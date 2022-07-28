package com.psw9999.android_accountbook_18.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val _payments = MutableStateFlow<List<PaymentDto>>(emptyList())
    val payments = _payments

    fun getAllPayments() {
        viewModelScope.launch {
            repository.getAllPayments().let { result ->
                if (result is Result.Success) {
                    _payments.value = result.data
                } else {
                    // TODO : DB Read 실패처리
                    _payments.value = emptyList()
                }
            }
        }
    }
}
