package com.psw9999.android_accountbook_18.data.repository.category

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.CategoryDto

interface CategoryRepository {

    suspend fun getAllCategorys() : Result<List<CategoryDto>>

    suspend fun saveCategory(isSpend: Boolean, title: String, color: Int)

    suspend fun updateCategory(isSpend: Boolean, title: String, color: Int)

}