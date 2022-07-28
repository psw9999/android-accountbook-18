package com.psw9999.android_accountbook_18.data.source.local.history

import android.content.ContentValues
import android.util.Log
import com.psw9999.android_accountbook_18.data.dto.HistoryDto
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.Result.Success
import com.psw9999.android_accountbook_18.data.Result.Error
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper.Companion.HISTORY_TABLE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class HistoryLocalDataSource @Inject constructor(
    private val dataBaseHelper: DatabaseHelper,
    private val ioDispatcher: CoroutineDispatcher
) : HistoryDataSource {

    override suspend fun saveHistory(historyDto: HistoryDto): Unit = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getMonthHistorys(date : String): Result<List<HistoryDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHistory(history: HistoryDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHistories(idList: List<Int>) {
        TODO("Not yet implemented")
    }
}