package com.psw9999.android_accountbook_18.ui.historyinput

import android.app.DatePickerDialog
import android.os.Bundle
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
    private val historyDataViewModel : HistoryDataViewModel by activityViewModels()

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
            if(historyItem.isSpend) historyInputViewModel.setPaymentMethod(Pair(historyItem.paymentId,historyItem.payment!!))
            setCategory(Pair(historyItem.categoryId,historyItem.category))
        }
    }

    override fun initViews() {
        binding.viewmodel = historyInputViewModel
        historyDataViewModel
        // 화면 config 대응
        val date = historyInputViewModel.historyDate.value.split("-")
        val datePicker = DatePickerDialog(
            activityContext, { _, year, month, dayOfMonth ->
                historyInputViewModel.setHistoryDate(year, month+1, dayOfMonth)
            },
            date[0].toInt(), date[1].toInt()-1, date[2].toInt()
        )

        binding.tvDate.setOnClickListener {
            datePicker.show()
        }

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
                        // TODO : 결제수단 추가하기 프래그먼트로 넘어가기
                        val transaction =
                            activity?.
                            supportFragmentManager?.
                            beginTransaction()?.
                            add(R.id.l_main_container, PaymentAddFragment())
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
                            activity?.
                            supportFragmentManager?.
                            beginTransaction()?.
                            add(R.id.l_main_container, CategoryAddFragment().apply {
                                arguments = Bundle().apply {
                                    putBoolean(IS_SPEND, historyInputViewModel.isSpend.value)
                                }
                            })
                        transaction!!.addToBackStack(null)
                        transaction.hide(this@HistoryInputFragment)
                        transaction.commit()
                        // TODO : 분류 추가하기 프래그먼트로 넘어가기
                    } else {
                        historyInputViewModel.setCategory(categoryAdapter.getItem(position))
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        binding.tbtngTypeSelector.addOnButtonCheckedListener { group, _, _ ->
            if (group.checkedButtonId == R.id.tbtn_spend) historyInputViewModel.setIsSpend(true)
            else historyInputViewModel.setIsSpend(false)
            if(!historyInputViewModel.isRevised.value) clearInputField()
        }

        binding.btnRegister.setOnClickListener {
            historyDataViewModel
            if(historyInputViewModel.isRevised.value) updateHistory()
            else saveHistory()
        }
    }

    private fun saveHistory() {
        CoroutineScope(Dispatchers.IO).launch {
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
            historyDataViewModel.getMonthHistorys(
                historyDataViewModel.selectedDate.value.year,
                historyDataViewModel.selectedDate.value.monthValue
            )
            withContext(Dispatchers.Main) {
                val fragmentManager = activity?.supportFragmentManager!!
                fragmentManager.beginTransaction().remove(this@HistoryInputFragment).commit()
                fragmentManager.popBackStack()
            }
        }
    }

    private fun updateHistory() {
        CoroutineScope(Dispatchers.IO).launch {
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
            historyDataViewModel.getMonthHistorys(
                historyDataViewModel.selectedDate.value.year,
                historyDataViewModel.selectedDate.value.monthValue
            )
            withContext(Dispatchers.Main) {
                val fragmentManager = activity?.supportFragmentManager!!
                fragmentManager.beginTransaction().remove(this@HistoryInputFragment).commit()
                fragmentManager.popBackStack()
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
            }
        }
    }
}