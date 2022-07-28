package com.psw9999.android_accountbook_18.ui.historyinput

import android.app.DatePickerDialog
import android.view.View
import android.widget.AdapterView
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
                if (position >= paymentViewModel.payments.value.size) {
                    // TODO : 결제수단 추가하기 프래그먼트로 넘어가기
                } else {
                    historyInputViewModel.setPaymentMethod(paymentViewModel.payments.value[position].method)
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
                if (position >= categoryViewModel.category.value.size) {
                    // TODO : 분류 추가하기 프래그먼트로 넘어가기
                } else {
                    historyInputViewModel.setCategory(categoryViewModel.category.value[position].name)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
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
                            it.forEach { payment ->
                                this.add(payment.name)
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
            }
        }
    }

}