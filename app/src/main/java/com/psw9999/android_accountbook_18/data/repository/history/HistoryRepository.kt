package com.psw9999.android_accountbook_18.data.repository.history

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.HistoryDto

interface HistoryRepository {

    suspend fun getHistorys() : Result<List<HistoryDto>>

    suspend fun saveHistory()

}