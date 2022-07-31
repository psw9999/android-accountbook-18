package com.psw9999.android_accountbook_18.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {
    val currentDate: LocalDate = LocalDate.now()
    const val dateFormat = "%d-%02d-%02d"
    const val dateAppbarFormat = "yyyy년 MM월"

    fun dateToHistoryHeaderDate(date: String): String {
        val historyHeaderFormat = DateTimeFormatter.ofPattern("MM월 dd일 E")
        return historyHeaderFormat.format(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
    }

}