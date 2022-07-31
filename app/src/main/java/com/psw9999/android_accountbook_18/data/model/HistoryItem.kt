package com.psw9999.android_accountbook_18.data.model

data class HistoryItem(
    val id: Int,
    val time: String,
    val amount: Int,
    val content: String,
    val payment : String?,
    val isSpend : Boolean,
    val category: String,
    val color: Int,
    var isLast : Boolean = false
)