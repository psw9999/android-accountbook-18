package com.psw9999.android_accountbook_18.data.model

sealed class CalendarListItem {
    data class CalendarContent(val calendarItem: CalendarItem) : CalendarListItem()
    object CalendarLoading : CalendarListItem()
}