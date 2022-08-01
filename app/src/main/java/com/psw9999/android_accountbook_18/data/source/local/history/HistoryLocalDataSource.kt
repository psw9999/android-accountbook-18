package com.psw9999.android_accountbook_18.data.source.local.history

import android.content.ContentValues
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.db.CategoryColumns
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper.Companion.CATEGORY_TABLE
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper.Companion.HISTORY_TABLE
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper.Companion.PAYMENT_TABLE
import com.psw9999.android_accountbook_18.data.db.HistoryColumns
import com.psw9999.android_accountbook_18.data.db.PaymentColumns
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.util.DateUtil.dateFormat
import com.psw9999.android_accountbook_18.util.toBoolean
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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

    override suspend fun getMonthHistorys(year: Int, month: Int): Result<List<HistoryItem>> =
        withContext(ioDispatcher) {
            val rd = dataBaseHelper.readableDatabase
            val cursor = rd.rawQuery(
                "SELECT $HISTORY_TABLE.${HistoryColumns.id.columnName}, ${HistoryColumns.time.columnName}, ${HistoryColumns.amount.columnName}, " +
                        "${HistoryColumns.content.columnName}, ${HistoryColumns.category_id.columnName}, ${HistoryColumns.payment_id.columnName}, " +
                        "${PaymentColumns.method.columnName}, ${CategoryColumns.is_spend.columnName}, ${CategoryColumns.title.columnName}, " +
                        "${CategoryColumns.color.columnName} " +
                        "FROM $HISTORY_TABLE " +
                        "LEFT JOIN $CATEGORY_TABLE ON $HISTORY_TABLE.category_id = $CATEGORY_TABLE.id " +
                        "LEFT JOIN $PAYMENT_TABLE ON $HISTORY_TABLE.payment_id = $PAYMENT_TABLE.id " +
                        "WHERE ${HistoryColumns.time.columnName} BETWEEN \"${String.format(dateFormat,year,month,1)}\" "+
                        "AND \"${String.format(dateFormat, year, month, 31)}\" " +
                        "ORDER BY ${HistoryColumns.time.columnName} desc", null
            )
            return@withContext try {
                val historyList = mutableListOf<HistoryItem>()
                while (cursor.moveToNext()) {
                    with(cursor) {
                        historyList.add(
                            HistoryItem(
                                getInt(getColumnIndexOrThrow(HistoryColumns.id.columnName)),
                                getString(getColumnIndexOrThrow(HistoryColumns.time.columnName)),
                                getInt(getColumnIndexOrThrow(HistoryColumns.amount.columnName)),
                                getString(getColumnIndexOrThrow(HistoryColumns.content.columnName)),
                                getInt(getColumnIndexOrThrow(HistoryColumns.payment_id.columnName)),
                                getString(getColumnIndexOrThrow(PaymentColumns.method.columnName)),
                                getInt(getColumnIndexOrThrow(HistoryColumns.category_id.columnName)),
                                getString(getColumnIndexOrThrow(CategoryColumns.title.columnName)),
                                getInt(getColumnIndexOrThrow(CategoryColumns.is_spend.columnName)).toBoolean(),
                                getInt(getColumnIndexOrThrow(CategoryColumns.color.columnName))
                            )
                        )
                    }
                }
                Result.Success(historyList)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(e)
            } finally {
                if (cursor != null && !cursor.isClosed) {
                    cursor.close()
                }
            }
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