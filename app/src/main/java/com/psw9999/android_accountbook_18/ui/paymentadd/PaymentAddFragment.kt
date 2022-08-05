package com.psw9999.android_accountbook_18.ui.paymentadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.databinding.FragmentPaymentAddBinding
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.main.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentAddFragment
    : BaseFragment<FragmentPaymentAddBinding>(R.layout.fragment_payment_add) {

    companion object {
        const val PAYMENT_ITEM = "PAYMENT_ITEM"
    }

    private val paymentAddViewModel: PaymentAddViewModel by viewModels()
    private val paymentViewModel: PaymentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.apply {
            setBefPaymentValue(this.getParcelable(PAYMENT_ITEM)!!)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setBefPaymentValue(paymentDto: PaymentDto) {
        with(paymentAddViewModel) {
            setIsRevising(true)
            setPaymentId(paymentDto.id)
            setPaymentTitle(paymentDto.method)
        }
    }

    private fun initAppbar() {
        binding.abAddPayment.setNavigationOnClickListener {
            fragmentTerminate()
        }
    }

    private fun fragmentTerminate() {
        val fragmentManager = activity!!.supportFragmentManager
        fragmentManager.beginTransaction().remove(this).commit()
        fragmentManager.popBackStack()
    }

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    paymentViewModel.isLoading.collectLatest { isLoading ->
                        if (isLoading) binding.pbPaymentLoading.visibility = View.VISIBLE
                        else binding.pbPaymentLoading.visibility = View.GONE
                    }
                }
                launch {
                    paymentViewModel.isComplete.collectLatest { isComplete ->
                        if (isComplete) {
                            paymentViewModel.setIsComplete(false)
                            fragmentTerminate()
                        }
                    }
                }
            }
        }
    }

    override fun initViews() {
        binding.viewModel = paymentAddViewModel
        initAppbar()
        binding.btnPaymentRegister.setOnClickListener {
            with(paymentAddViewModel) {
                if (isRevising.value!!) {
                    paymentViewModel.updatePayment(
                        PaymentDto(
                            id = paymentId.value!!,
                            method = paymentTitle.value!!
                        )
                    )
                } else {
                    paymentViewModel.savePayment(
                        paymentTitle.value!!
                    )
                }
            }
        }
    }
}