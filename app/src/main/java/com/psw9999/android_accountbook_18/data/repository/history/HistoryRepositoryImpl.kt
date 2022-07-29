package com.psw9999.android_accountbook_18.data.repository.history

import com.psw9999.android_accountbook_18.data.source.local.history.HistoryDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDataSource: HistoryDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : HistoryRepository {

    override suspend fun saveHistory(
        time: String,
        amount: Int,
        content: String,
        paymentId: Int?,
        categoryId: Int
    ) {
        historyDataSource.saveHistory(time, amount, content, paymentId, categoryId)
    }

}