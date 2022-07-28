package com.psw9999.android_accountbook_18.data.source.local.history

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.HistoryDto

interface HistoryDataSource {

    suspend fun getMonthHistorys(date: String): Result<List<HistoryDto>>

    suspend fun saveHistory(history: HistoryDto)

    suspend fun updateHistory(history: HistoryDto)

    suspend fun deleteHistories(idList: List<Int>)

}