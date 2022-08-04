package com.psw9999.android_accountbook_18.ui.historyinput

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.dto.HistoryDto
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.databinding.FragmentHistoryInputBinding
import com.psw9999.android_accountbook_18.ui.categoryadd.CategoryAddFragment
import com.psw9999.android_accountbook_18.ui.categoryadd.CategoryAddFragment.Companion.IS_SPEND
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.common.customview.HistoryInputSpinner
import com.psw9999.android_accountbook_18.ui.historyinput.adapter.InputSpinnerAdapter
import com.psw9999.android_accountbook_18.ui.main.CategoryViewModel
import com.psw9999.android_accountbook_18.ui.main.HistoryDataViewModel
import com.psw9999.android_accountbook_18.ui.main.PaymentViewModel
import com.psw9999.android_accountbook_18.ui.paymentadd.PaymentAddFragment
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import com.psw9999.android_accountbook_18.util.toInt
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HistoryInputFragment :
    BaseFragment<FragmentHistoryInputBinding>(R.layout.fragment_history_input) {

    companion object {
        val spinnerInitValue = Pair(0, "")
        const val HISTORY_ITEM = "HISTORY_ITEM"
    }

    private val historyInputViewModel: HistoryInputViewModel by viewModels()
    private val paymentViewModel: PaymentViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val historyDataViewModel: HistoryDataViewModel by activityViewModels()

    private val paymentAdapter by lazy { InputSpinnerAdapter(activityContext) }
    private val categoryAdapter by lazy { InputSpinnerAdapter(activityContext) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.apply {
            setBefHistoryValue(this.getParcelable(HISTORY_ITEM)!!)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setBefHistoryValue(historyItem: HistoryItem) {
        with(historyInputViewModel) {
            setIsRevised(true)
            setHistoryId(historyItem.id)
            setHistoryDate(historyItem.time)
            setIsSpend(historyItem.isSpend)
            setAmount(historyItem.amount.toString())
            setContent(historyItem.content)
            if (historyItem.isSpend) historyInputViewModel.setPaymentMethod(
                Pair(
                    historyItem.paymentId,
                    historyItem.payment!!
                )
            )
            setCategory(Pair(historyItem.categoryId, historyItem.category))
        }
    }

    private fun initDatePicker() {
        val date = historyInputViewModel.historyDate.value.split("-")
        val datePicker = DatePickerDialog(
            activityContext, { _, year, month, dayOfMonth ->
                historyInputViewModel.setHistoryDate(year, month + 1, dayOfMonth)
            },
            date[0].toInt(), date[1].toInt() - 1, date[2].toInt()
        )

        binding.tvDate.setOnClickListener {
            datePicker.show()
        }
    }

    private fun amountConversion() {
        binding.edtAmount.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(e: Editable?) {
                    if (!binding.edtAmount.text.isNullOrEmpty()) {
                        binding.edtAmount.removeTextChangedListener(this)
                        val amount = binding.edtAmount.text.toString().replace(",", "")
                        historyInputViewModel.setAmount(amount)

                        val parsing = getString(R.string.input_amount).format(amount.toInt())
                        binding.edtAmount.setText(parsing)
                        binding.edtAmount.setSelection(parsing.length)
                        binding.edtAmount.addTextChangedListener(this)
                    }
                }
            })

        if (historyInputViewModel.amount.value.isNotEmpty()) {
            binding.edtAmount.setText(historyInputViewModel.amount.value)
        }
    }

    private fun initSpinner() {
        binding.spRegisterPayment.adapter = paymentAdapter
        binding.spRegisterCategory.adapter = categoryAdapter

        binding.spRegisterPayment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == paymentAdapter.count - 1) {
                        val transaction =
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.add(R.id.l_main_container, PaymentAddFragment())
                        transaction!!.addToBackStack(null)
                        transaction.hide(this@HistoryInputFragment)
                        transaction.commit()
                    } else {
                        historyInputViewModel.setPaymentMethod(paymentAdapter.getItem(position))
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        binding.spRegisterCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position >= categoryAdapter.count - 1) {
                        val transaction =
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.add(R.id.l_main_container, CategoryAddFragment().apply {
                                    arguments = Bundle().apply {
                                        putBoolean(IS_SPEND, historyInputViewModel.isSpend.value)
                                    }
                                })
                        transaction!!.addToBackStack(null)
                        transaction.hide(this@HistoryInputFragment)
                        transaction.commit()
                    } else {
                        historyInputViewModel.setCategory(categoryAdapter.getItem(position))
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        binding.spRegisterCategory.setOnDropDownListener(object : HistoryInputSpinner.OnDropDownEventsListener {
            override fun dropDownOpen() {
                categoryAdapter.setIsSelected(true)
            }

            override fun dropDownClose() {
                categoryAdapter.setIsSelected(false)
            }
        })


        binding.spRegisterPayment.setOnDropDownListener(object : HistoryInputSpinner.OnDropDownEventsListener {
            override fun dropDownOpen() {
                paymentAdapter.setIsSelected(true)
            }

            override fun dropDownClose() {
                paymentAdapter.setIsSelected(false)
            }
        })
    }


    override fun initViews() {
        binding.viewmodel = historyInputViewModel

        initDatePicker()
        amountConversion()
        initSpinner()

        binding.tbtngTypeSelector.addOnButtonCheckedListener { group, _, _ ->
            if (group.checkedButtonId == R.id.tbtn_spend) historyInputViewModel.setIsSpend(true)
            else historyInputViewModel.setIsSpend(false)
            if (!historyInputViewModel.isRevised.value) clearInputField()
        }

        binding.btnRegister.setOnClickListener {
            if (historyInputViewModel.isRevised.value) {
                with(historyInputViewModel) {
                    historyDataViewModel.updateHistory(
                        HistoryDto(
                            id = historyId.value,
                            time = historyDate.value,
                            amount = amount.value.toInt(),
                            content = content.value,
                            paymentId = if (!isSpend.value) null
                            else paymentMethod.value.first,
                            categoryId = if (catergory.value.first == 0) 1 + (isSpend.value.toInt())
                            else catergory.value.first
                        )
                    )
                }
            } else {
                with(historyInputViewModel) {
                    historyDataViewModel.saveHistory(
                        time = historyDate.value,
                        amount = amount.value.toInt(),
                        content = content.value,
                        paymentId = if (!isSpend.value) null
                        else paymentMethod.value.first,
                        categoryId = if (catergory.value.first == 0) 1
                        else catergory.value.first
                    )
                }
            }
        }
    }

    // 탭 전환시 기존 입력 항목 초기화
    private fun clearInputField() {
        with(binding) {
            historyInputViewModel.setHistoryDate(
                currentDate.year,
                currentDate.monthValue,
                currentDate.dayOfMonth
            )

            historyInputViewModel.setPaymentMethod(spinnerInitValue)
            historyInputViewModel.setCategory(spinnerInitValue)

            categoryAdapter.setSpinnerList(categoryFilter())
            edtAmount.editableText.clear()
            edtRegisterContent.editableText.clear()
        }
    }

    private fun categoryFilter(): MutableList<Pair<Int, String>> {
        val categorys = mutableListOf<Pair<Int, String>>().apply {
            categoryViewModel.category.value.filter { category ->
                category.isSpend == historyInputViewModel.isSpend.value
            }.forEach {
                this.add(Pair(it.id, it.name))
            }
        }
        return categorys
    }

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    paymentViewModel.payments.collectLatest {
                        val payments = mutableListOf<Pair<Int, String>>().apply {
                            it.forEach { payment ->
                                this.add(Pair(payment.id, payment.method))
                            }
                        }
                        paymentAdapter.setSpinnerList(payments)
                    }
                }

                launch {
                    categoryViewModel.category.collectLatest {
                        categoryAdapter.setSpinnerList(categoryFilter())
                    }
                }

                launch {
                    historyInputViewModel.paymentMethod.collectLatest {
                        paymentAdapter.setSelectedValue(it)
                    }
                }

                launch {
                    historyInputViewModel.catergory.collectLatest {
                        categoryAdapter.setSelectedValue(it)
                    }
                }

                launch {
                    historyInputViewModel.stateCombine.collectLatest {
                        historyInputViewModel.setIsRegisterEnabled(it)
                    }
                }

                launch {
                    historyDataViewModel.isComplete.collectLatest { isComplete ->
                        if (isComplete) {
                            historyDataViewModel.setIsComplete(false)
                            val fragmentManager = activity?.supportFragmentManager!!
                            fragmentManager.beginTransaction().remove(this@HistoryInputFragment).commit()
                            fragmentManager.popBackStack()
                        }
                    }
                }
            }
        }
    }
}