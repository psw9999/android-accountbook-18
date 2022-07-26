package com.psw9999.android_accountbook_18.ui.main

import com.psw9999.android_accountbook_18.R

enum class MainBottomMenuType(val tag: String, val id: Int) {
    HISTORY("History", R.id.bottom_menu_history),
    CALENDAR("Calendar", R.id.bottom_menu_calendar),
    STATISTICS("Statistics", R.id.bottom_menu_statistics),
    CONFIGURATION("Configuration", R.id.bottom_menu_configuration)
}