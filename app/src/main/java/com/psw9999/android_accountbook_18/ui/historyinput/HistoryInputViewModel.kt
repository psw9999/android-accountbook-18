package com.psw9999.android_accountbook_18.ui.historyinput

import androidx.lifecycle.ViewModel
import com.psw9999.android_accountbook_18.util.DateUtil.dateFormat
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class HistoryInputViewModel : ViewModel() {

    val currentDate: Calendar = Calendar.getInstance()

    private val _historyDate = MutableStateFlow(
        String.format(
            dateFormat,
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH) + 1,
            currentDate.get(Calendar.DAY_OF_MONTH)))
    val historyDate = _historyDate

    fun setHistoryDate(year: Int, month: Int, dayOfMonth: Int) {
        historyDate.value = String.format(dateFormat, year, month + 1, dayOfMonth)
    }

}