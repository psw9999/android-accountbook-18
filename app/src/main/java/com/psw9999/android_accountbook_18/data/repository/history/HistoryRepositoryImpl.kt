package com.psw9999.android_accountbook_18.data.repository.history

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.source.local.history.HistoryDataSource
import com.psw9999.android_accountbook_18.data.vo.HistoryVo
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDataSource: HistoryDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : HistoryRepository {

    override suspend fun getMonthHistorys(year: Int, month: Int): Result<List<HistoryVo>> =
        historyDataSource.getMonthHistorys(year, month)


    override suspend fun saveHistory(
        time: String,
        amount: Int,
        content: String,
        paymentId: Int?,
        categoryId: Int
    ) {
        historyDataSource.saveHistory(time, amount, content, paymentId, categoryId)
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