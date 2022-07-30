package com.psw9999.android_accountbook_18.ui.paymentadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentAddViewModel : ViewModel() {

    private val _paymentTitle = MutableLiveData("")
    val paymentTitle: LiveData<String> = _paymentTitle

    fun setPaymentTitle(paymentTitle: String) {
        _paymentTitle.value = paymentTitle
    }
}