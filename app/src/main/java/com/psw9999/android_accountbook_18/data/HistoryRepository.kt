package com.psw9999.android_accountbook_18.data

import com.psw9999.android_accountbook_18.data.dto.HistoryDto

interface HistoryRepository {

    suspend fun getHistorys() : Result<List<HistoryDto>>

    suspend fun saveHistory()

}