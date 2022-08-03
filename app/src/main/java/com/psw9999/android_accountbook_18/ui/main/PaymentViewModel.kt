package com.psw9999.android_accountbook_18.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.data.repository.payment.PaymentRepository
import com.psw9999.android_accountbook_18.util.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val _payments = MutableStateFlow<List<PaymentDto>>(emptyList())
    val payments : StateFlow<List<PaymentDto>> = _payments

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _isComplete = MutableStateFlow(false)
    val isComplete : StateFlow<Boolean> = _isComplete

    fun setIsComplete(isComplete : Boolean) {
        _isComplete.value = isComplete
    }

    fun getAllPayments() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getAllPayments().let { result ->
                if (result is Result.Success) {
                    _payments.value = result.data
                } else {
                    toast("결제수단 로드에 실패하였습니다.")
                    _payments.value = emptyList()
                }
                _isLoading.value = false
            }
        }
    }

    fun savePayment(title : String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.savePayment(title).let { result ->
                if (result is Result.Success) {
                    _isComplete.value = true
                    toast("결제수단을 저장하였습니다.")
                } else {
                    toast("결제수단 저장에 실패하였습니다.")
                    _payments.value = emptyList()
                }
                _isLoading.value = false
            }
        }
    }

    fun updatePayment(paymentDto : PaymentDto) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.updatePayment(paymentDto).let { result ->
                if (result is Result.Success) {
                    _isComplete.value = true
                    toast("결제수단을 수정하였습니다.")
                } else {
                    toast("결제수단 수정에 실패하였습니다.")
                    _payments.value = emptyList()
                }
                _isLoading.value = false
            }
        }
    }

}
