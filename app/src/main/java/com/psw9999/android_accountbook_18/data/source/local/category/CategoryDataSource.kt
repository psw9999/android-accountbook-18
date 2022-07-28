package com.psw9999.android_accountbook_18.data.source.local.category

import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.CategoryDto

interface CategoryDataSource {

    suspend fun getAllCategorys() : Result<List<CategoryDto>>

    suspend fun saveCategorys(isSpend : Boolean, title : String, color : Int)

    suspend fun updatePayment(isSpend : Boolean, title : String, color : Int)

}