package com.psw9999.android_accountbook_18.data.dto

data class HistoryDto(
    val id : Int,
    var time : String,
    var amount : Int,
    var content : String,
    var paymentId : Int,
    var categoryId : Int
)
