package com.psw9999.android_accountbook_18.data

import com.psw9999.android_accountbook_18.data.dto.HistoryDto
import com.psw9999.android_accountbook_18.data.source.local.history.HistoryDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDataSource: HistoryDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : HistoryRepository {

    override suspend fun getHistorys(): Result<List<HistoryDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveHistory() {
        TODO("Not yet implemented")
    }
}