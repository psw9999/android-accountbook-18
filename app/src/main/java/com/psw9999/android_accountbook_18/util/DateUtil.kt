package com.psw9999.android_accountbook_18.util

import java.time.LocalDate

object DateUtil {
    val currentDate: LocalDate = LocalDate.now()
    const val dateFormat = "%d-%02d-%02d"
    const val dateAppbarFormat = "yyyy년 MM월"
}