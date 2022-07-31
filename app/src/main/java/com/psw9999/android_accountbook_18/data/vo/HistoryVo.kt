package com.psw9999.android_accountbook_18.data.vo

data class HistoryVo(
    val id: Int,
    val time: String,
    val amount: Int,
    val content: String,
    val payment : String?,
    val isSpend : Boolean,
    val category: String,
    val color: Int
)