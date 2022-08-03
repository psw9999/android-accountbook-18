package com.psw9999.android_accountbook_18.ui.paymentadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentAddViewModel : ViewModel() {

    private val _paymentTitle = MutableLiveData("")
    val paymentTitle: LiveData<String> = _paymentTitle

    private val _isRevising = MutableLiveData(false)
    val isRevising : LiveData<Boolean> = _isRevising

    private val _paymentId = MutableLiveData(0)
    val paymentId: LiveData<Int> = _paymentId

    fun setPaymentTitle(paymentTitle: String) {
        _paymentTitle.value = paymentTitle
    }

    fun setIsRevising(isRevising: Boolean) {
        _isRevising.value = isRevising
    }

    fun setPaymentId(paymentId: Int) {
        _paymentId.value = paymentId
    }
}