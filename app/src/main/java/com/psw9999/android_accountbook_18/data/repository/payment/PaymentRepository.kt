package com.psw9999.android_accountbook_18.data.repository.payment

import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.data.Result

interface PaymentRepository {

    suspend fun getAllPayments(): Result<List<PaymentDto>>

    suspend fun savePayment(title: String): Result<Boolean>

    suspend fun updatePayment(paymentDto: PaymentDto): Result<Boolean>

}