package com.psw9999.android_accountbook_18.ui.historyinput

import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.FragmentHistoryInputBinding
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.historyinput.adapter.InputSpinnerAdapter
import com.psw9999.android_accountbook_18.ui.main.CategoryViewModel
import com.psw9999.android_accountbook_18.ui.main.PaymentViewModel
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

@AndroidEntryPoint
class HistoryInputFragment :
    BaseFragment<FragmentHistoryInputBinding>(R.layout.fragment_history_input) {

    private val historyInputViewModel : HistoryInputViewModel by viewModels()
    private val paymentViewModel : PaymentViewModel by activityViewModels()
    private val categoryViewModel : CategoryViewModel by activityViewModels()

    private val paymentAdapter by lazy { InputSpinnerAdapter(activityContext) }
    private val categoryAdapter by lazy { InputSpinnerAdapter(activityContext) }

    override fun initViews() {
        binding.viewmodel = historyInputViewModel

        // 화면 config 대응
        val date = historyInputViewModel.historyDate.value.split("-")
        val datePicker = DatePickerDialog(
            activityContext, { _, year, month, dayOfMonth ->
                historyInputViewModel.setHistoryDate(year, month, dayOfMonth) },
            date[0].toInt(), date[1].toInt()-1, date[2].toInt())

        binding.tvDate.setOnClickListener {
            datePicker.show()
        }

        binding.spRegisterPayment.adapter = paymentAdapter
        binding.spRegisterCategory.adapter = categoryAdapter

        binding.spRegisterPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == paymentAdapter.count-1) {
                    // TODO : 결제수단 추가하기 프래그먼트로 넘어가기
                } else {
                    historyInputViewModel.setPaymentMethod(paymentAdapter.getItem(position))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.spRegisterCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position >= categoryAdapter.count-1) {
                    // TODO : 분류 추가하기 프래그먼트로 넘어가기
                } else {
                    historyInputViewModel.setCategory(categoryAdapter.getItem(position))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.tbtngTypeSelector.addOnButtonCheckedListener { group, _, _ ->
            if(group.checkedButtonId == R.id.tbtn_spend) historyInputViewModel.setIsSpend(true)
            else historyInputViewModel.setIsSpend(false)
        }

        binding.edtAmount.addTextChangedListener {
            historyInputViewModel.setAmount(it.toString())
        }

        binding.edtRegisterContent.addTextChangedListener {
            historyInputViewModel.setContent(it.toString())
        }
    }

    // 탭 전환시 기존 입력 항목 초기화
    private fun clearInputField(isSpend : Boolean) {
        with(binding) {
            historyInputViewModel.setHistoryDate(
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH))

            historyInputViewModel.setPaymentMethod("")
            historyInputViewModel.setCategory("")

            val categorys = mutableListOf<String>().apply {
                categoryViewModel.category.value.filter { category ->
                    category.isSpend == isSpend
                }.forEach {
                    this.add(it.name)
                }
            }

            categoryAdapter.setSpinnerList(categorys)
            edtAmount.editableText.clear()
            edtRegisterContent.editableText.clear()
        }
    }



    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    paymentViewModel.payments.collectLatest {
                        val payments = mutableListOf<String>().apply {
                            it.forEach { payment ->
                                this.add(payment.method)
                            }
                        }
                        paymentAdapter.setSpinnerList(payments)
                    }
                }

                launch {
                    categoryViewModel.category.collectLatest {
                        val categorys = mutableListOf<String>().apply {
                            it.filter { category ->
                                category.isSpend == historyInputViewModel.isSpend.value
                            }.forEach {
                                this.add(it.name)
                            }
                        }
                        categoryAdapter.setSpinnerList(categorys)
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
                    historyInputViewModel.isSpend.collectLatest {
                        clearInputField(it)
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