package com.psw9999.android_accountbook_18.data.model

import java.time.LocalDate

data class CalendarItem(
    val date : LocalDate,
    val isSameMonth : Boolean,
    val isToday : Boolean,
    var incomeAmount : Int = 0,
    var spendAmount : Int = 0,
    var SumAmount : Int = 0
)