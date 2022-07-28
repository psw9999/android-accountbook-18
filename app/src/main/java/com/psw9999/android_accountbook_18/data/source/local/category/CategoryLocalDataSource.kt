package com.psw9999.android_accountbook_18.data.source.local.category

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.db.CategoryColumns
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
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

        return@withContext try {
            val cursor = rd.rawQuery("SELECT * FROM ${DatabaseHelper.CATEGORY_TABLE}", null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(CategoryColumns.id.ordinal)
                val isSpend = cursor.getInt(CategoryColumns.is_spend.ordinal) == 1
                val name = cursor.getString(CategoryColumns.title.ordinal)
                val color = cursor.getInt(CategoryColumns.color.ordinal)
                categoryList.add(CategoryDto(id, isSpend, name, color))
            }
            rd.close()
            Result.Success(categoryList)
        } catch (e: Exception) {
            rd.close()
            Result.Error(e)
        }
    }

    override suspend fun saveCategorys(isSpend: Boolean, title: String, color: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePayment(isSpend: Boolean, title: String, color: Int) {
        TODO("Not yet implemented")
    }
}