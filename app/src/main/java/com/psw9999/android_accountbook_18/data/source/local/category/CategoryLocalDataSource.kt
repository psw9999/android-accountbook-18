package com.psw9999.android_accountbook_18.data.source.local.category

import android.content.ContentValues
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.db.CategoryColumns
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper.Companion.CATEGORY_TABLE
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
import com.psw9999.android_accountbook_18.util.toInt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
                categoryList.add(
                    CategoryDto(
                        cursor.getInt(CategoryColumns.id.ordinal),
                        cursor.getInt(CategoryColumns.is_spend.ordinal) == 1,
                        cursor.getString(CategoryColumns.title.ordinal),
                        color = cursor.getInt(CategoryColumns.color.ordinal)
                    )
                )
            }
            cursor.close()
            Result.Success(categoryList)
        } catch (e: Exception) {
            cursor.close()
            Result.Error(e)
        }
    }

    override suspend fun saveCategorys(isSpend: Boolean, title: String, color: Int): Result<Boolean> =
        withContext(ioDispatcher) {
            return@withContext try {
                val wd = dataBaseHelper.writableDatabase
                val categoryValues = ContentValues().apply {
                    put(CategoryColumns.is_spend.columnName, isSpend.toInt())
                    put(CategoryColumns.title.columnName, title)
                    put(CategoryColumns.color.columnName, color)
                }
                wd.insert(CATEGORY_TABLE, null, categoryValues)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun updatePayment(categoryDto: CategoryDto): Result<Boolean> =
        withContext(ioDispatcher) {
            return@withContext try {
                val wd = dataBaseHelper.writableDatabase
                val categoryValues = ContentValues().apply {
                    put(CategoryColumns.id.columnName, categoryDto.id)
                    put(CategoryColumns.title.columnName, categoryDto.name)
                    put(CategoryColumns.color.columnName, categoryDto.color)
                    put(CategoryColumns.is_spend.columnName, categoryDto.isSpend)
                }
                wd.update(
                    CATEGORY_TABLE,
                    categoryValues,
                    "${CategoryColumns.id.columnName} = ${categoryDto.id}",
                    null
                )
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}

