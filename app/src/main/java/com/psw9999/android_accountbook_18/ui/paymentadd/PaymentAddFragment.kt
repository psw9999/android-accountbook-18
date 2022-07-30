package com.psw9999.android_accountbook_18.ui.paymentadd

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.FragmentPaymentAddBinding
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.main.PaymentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentAddFragment
    : BaseFragment<FragmentPaymentAddBinding>(R.layout.fragment_payment_add) {

    private val paymentAddViewModel : PaymentAddViewModel by viewModels()
    private val paymentViewModel: PaymentViewModel by activityViewModels()

    override fun observe() {
    }

    override fun initViews() {
        binding.viewModel = paymentAddViewModel
        binding.edtPayment.addTextChangedListener {
            paymentAddViewModel.setPaymentTitle(it.toString())
        }

        binding.btnPaymentRegister.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                paymentViewModel.savePayment(paymentAddViewModel.paymentTitle.value!!)
                paymentViewModel.getAllPayments()
                // TODO : 프래그먼트 종료 로직 개선
                val fragmentManager = activity?.supportFragmentManager!!
                fragmentManager.beginTransaction().remove(this@PaymentAddFragment).commit()
                fragmentManager.popBackStack()
            }
        }
    }
}