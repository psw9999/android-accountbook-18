package com.psw9999.android_accountbook_18.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryItem(
    val id: Int,
    val time: String,
    val amount: Int,
    val content: String,
    val paymentId : Int,
    val payment : String?,
    val categoryId : Int,
    val category: String,
    val isSpend : Boolean,
    val color: Int,
    var isLast : Boolean = false
) : Parcelable