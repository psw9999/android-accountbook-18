package com.psw9999.android_accountbook_18.data.source.local.payment

import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.data.Result

interface PaymentDataSource {

    suspend fun getAllPayments() : Result<List<PaymentDto>>

    suspend fun savePayment(title : String)

    suspend fun updatePayment(title : String)

}