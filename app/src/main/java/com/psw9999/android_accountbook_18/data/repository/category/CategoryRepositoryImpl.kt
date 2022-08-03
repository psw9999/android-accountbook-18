package com.psw9999.android_accountbook_18.data.repository.category

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
import com.psw9999.android_accountbook_18.data.source.local.category.CategoryDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDataSource: CategoryDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : CategoryRepository {

    override suspend fun getAllCategorys(): Result<List<CategoryDto>>
        = categoryDataSource.getAllCategorys()

    override suspend fun saveCategory(isSpend: Boolean, title: String, color: Int) : Result<Boolean> =
        categoryDataSource.saveCategorys(isSpend, title, color)


    override suspend fun updateCategory(categoryDto: CategoryDto) : Result<Boolean> =
        categoryDataSource.updatePayment(categoryDto)

}