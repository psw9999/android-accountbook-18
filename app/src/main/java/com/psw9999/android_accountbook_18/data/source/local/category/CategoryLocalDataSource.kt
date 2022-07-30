package com.psw9999.android_accountbook_18.data.source.local.category

import android.content.ContentValues
import android.util.Log
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.db.CategoryColumns
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper.Companion.CATEGORY_TABLE
import com.psw9999.android_accountbook_18.data.db.PaymentColumns
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
import com.psw9999.android_accountbook_18.util.toInt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val dataBaseHelper: DatabaseHelper,
    private val ioDispatcher: CoroutineDispatcher
) : CategoryDataSource {

    override suspend fun getAllCategorys(): Result<List<CategoryDto>> = withContext(ioDispatcher) {
        val rd = dataBaseHelper.readableDatabase
        val categoryList = mutableListOf<CategoryDto>()
        val cursor = rd.rawQuery("SELECT * FROM $CATEGORY_TABLE", null)
        return@withContext try {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(CategoryColumns.id.ordinal)
                val isSpend = cursor.getInt(CategoryColumns.is_spend.ordinal) == 1
                val name = cursor.getString(CategoryColumns.title.ordinal)
                val color = cursor.getInt(CategoryColumns.color.ordinal)
                categoryList.add(CategoryDto(id, isSpend, name, color))
            }
            cursor.close()
            Result.Success(categoryList)
        } catch (e: Exception) {
            cursor.close()
            Result.Error(e)
        }
    }

    override suspend fun saveCategorys(isSpend: Boolean, title: String, color: Int) {
        withContext(ioDispatcher) {
            val wd = dataBaseHelper.writableDatabase
            val categoryValues = ContentValues().apply {
                put(CategoryColumns.is_spend.columnName, isSpend.toInt())
                put(CategoryColumns.title.columnName, title)
                put(CategoryColumns.color.columnName, color)
            }
            wd.insert(CATEGORY_TABLE, null, categoryValues)
        }
    }

    override suspend fun updatePayment(isSpend: Boolean, title: String, color: Int) {
        TODO("Not yet implemented")
    }
}

