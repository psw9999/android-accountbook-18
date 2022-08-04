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

    fun dateToHistoryInputDate(date: String): String {
        val historyHeaderFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd E요일")
        return historyHeaderFormat.format(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
    }

    fun getDayValue(date: String) =
        LocalDate.parse(date, DateTimeFormatter.ISO_DATE).dayOfMonth

    fun isSameMonth(first: LocalDate, second: LocalDate): Boolean =
        first.year == second.year && first.monthValue == second.monthValue

    fun isSameDay(date : LocalDate) : Boolean =
        date.year == currentDate.year && date.monthValue == currentDate.monthValue && date.dayOfMonth == currentDate.dayOfMonth

}