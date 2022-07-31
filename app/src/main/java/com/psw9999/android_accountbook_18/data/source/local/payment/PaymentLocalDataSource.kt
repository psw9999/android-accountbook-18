package com.psw9999.android_accountbook_18.data.source.local.payment

import android.content.ContentValues
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.Result.Success
import com.psw9999.android_accountbook_18.data.Result.Error
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper.Companion.PAYMENT_TABLE
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
        val cursor = rd.rawQuery("SELECT * FROM $PAYMENT_TABLE", null)
        return@withContext try {
            while (cursor.moveToNext()) {
                paymentList.add(
                    PaymentDto(
                        cursor.getInt(PaymentColumns.id.ordinal),
                        cursor.getString(PaymentColumns.method.ordinal)
                    )
                )
            }
            cursor.close()
            Success(paymentList)
        } catch (e: Exception) {
            cursor.close()
            Error(e)
        }
    }

    override suspend fun savePayment(title: String) {
        withContext(ioDispatcher) {
            val wd = dataBaseHelper.writableDatabase
            val paymentValues = ContentValues().apply {
                put(PaymentColumns.method.columnName, title)
            }
            wd.insert(PAYMENT_TABLE, null, paymentValues)
        }
    }

    override suspend fun updatePayment(title: String) {
        TODO("Not yet implemented")
    }
}