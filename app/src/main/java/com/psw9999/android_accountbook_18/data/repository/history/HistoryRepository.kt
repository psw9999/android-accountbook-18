package com.psw9999.android_accountbook_18.data.repository.history

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.model.HistoryItem

interface HistoryRepository {

    suspend fun getMonthHistorys(year : Int, month : Int): Result<List<HistoryItem>>

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