package com.psw9999.android_accountbook_18.data.source.local.history

import android.content.ContentValues
import android.util.Log
import com.psw9999.android_accountbook_18.data.dto.HistoryDto
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.Result.Success
import com.psw9999.android_accountbook_18.data.Result.Error
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper.Companion.HISTORY_TABLE
import com.psw9999.android_accountbook_18.data.db.HistoryColumns
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class HistoryLocalDataSource @Inject constructor(
    private val dataBaseHelper: DatabaseHelper,
    private val ioDispatcher: CoroutineDispatcher
) : HistoryDataSource {

    override suspend fun saveHistory(
        time: String,
        amount: Int,
        content: String,
        paymentId: Int?,
        categoryId: Int
    ) {
        withContext(ioDispatcher) {
            val wd = dataBaseHelper.writableDatabase
            val historyValues = ContentValues().apply {
                put(HistoryColumns.time.columnName, time)
                put(HistoryColumns.amount.columnName, amount)
                put(HistoryColumns.content.columnName, content)
                put(HistoryColumns.category_id.columnName, categoryId)
                put(HistoryColumns.payment_id.columnName, paymentId)
            }
            wd.insert(HISTORY_TABLE, null, historyValues)
        }
    }

    override suspend fun getMonthHistorys(date : String): Result<List<HistoryDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHistory(
        time: String,
        amount: Int,
        content: String,
        paymentId: Int?,
        categoryId: Int
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHistories(idList: List<Int>) {
        TODO("Not yet implemented")
    }
}