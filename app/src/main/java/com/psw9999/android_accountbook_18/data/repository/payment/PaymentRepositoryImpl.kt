package com.psw9999.android_accountbook_18.data.repository.payment

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.data.source.local.payment.PaymentDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val PaymentDataSource: PaymentDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : PaymentRepository {

    override suspend fun getAllPayments(): Result<List<PaymentDto>> =
        PaymentDataSource.getAllPayments()

    override suspend fun savePayment(title: String): Result<Boolean> =
        PaymentDataSource.savePayment(title)

    override suspend fun updatePayment(paymentDto: PaymentDto): Result<Boolean> =
        PaymentDataSource.updatePayment(paymentDto)

}