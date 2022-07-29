package com.psw9999.android_accountbook_18.data.source.local.history

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.HistoryDto

interface HistoryDataSource {

    suspend fun getMonthHistorys(date: String): Result<List<HistoryDto>>

    suspend fun saveHistory(
        time : String,
        amount : Int,
        content : String,
        paymentId : Int?,
        categoryId : Int
    )

    suspend fun updateHistory(
        time : String,
        amount : Int,
        content : String,
        paymentId : Int?,
        categoryId : Int
    )

    suspend fun deleteHistories(idList: List<Int>)

}