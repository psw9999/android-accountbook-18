package com.psw9999.android_accountbook_18.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDto(
    val id: Int,
    var method: String
) : Parcelable