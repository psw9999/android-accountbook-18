package com.psw9999.android_accountbook_18.data.source.local.payment

import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.Result.Success
import com.psw9999.android_accountbook_18.data.Result.Error
import com.psw9999.android_accountbook_18.data.db.PaymentColumns
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class PaymentLocalDataSource @Inject constructor(
    private val dataBaseHelper: DatabaseHelper,
    private val ioDispatcher: CoroutineDispatcher
) : PaymentDataSource {

    override suspend fun getAllPayments(): Result<List<PaymentDto>> = withContext(ioDispatcher) {
        val rd = dataBaseHelper.readableDatabase
        val paymentList = mutableListOf<PaymentDto>()
        return@withContext try {
            val cursor = rd.rawQuery("SELECT * FROM ${DatabaseHelper.PAYMENT_TABLE}", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(PaymentColumns.id.ordinal)
                val payment = cursor.getString(PaymentColumns.method.ordinal)
                paymentList.add(PaymentDto(id, payment))
            }
            rd.close()
            Success(paymentList)
        } catch (e: Exception) {
            rd.close()
            Error(e)
        }
    }

    override suspend fun savePayment(title: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePayment(title: String) {
        TODO("Not yet implemented")
    }
}