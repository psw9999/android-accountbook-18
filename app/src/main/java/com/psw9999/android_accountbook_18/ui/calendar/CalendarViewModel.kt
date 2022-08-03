package com.psw9999.android_accountbook_18.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psw9999.android_accountbook_18.data.model.CalendarItem
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import com.psw9999.android_accountbook_18.util.DateUtil.getDayValue
import com.psw9999.android_accountbook_18.util.DateUtil.isSameDay
import com.psw9999.android_accountbook_18.util.DateUtil.isSameMonth
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class CalendarViewModel : ViewModel() {

    companion object {
        const val WEEKS_PER_MONTH = 6
        const val DAYS_PER_WEEK = 7
        const val TOTAL_DAY = WEEKS_PER_MONTH * DAYS_PER_WEEK
    }

    private var _currentCalendarDate : LocalDate = currentDate

    private var _calendarList : List<CalendarItem> = getMonthList(currentDate)

    private var startMonthIndex = 0

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private fun setCurrentCalendarDate(selectedDate : LocalDate) {
        _currentCalendarDate = selectedDate
    }

    fun getCalendarData(selectedDate: LocalDate, historyList: List<HistoryItem>) : Deferred<List<CalendarItem>> {
        _isLoading.value = true
        return viewModelScope.async(Dispatchers.Default) {
            if ((selectedDate.year != _currentCalendarDate.year) || (selectedDate.monthValue != _currentCalendarDate.monthValue)) {
                setCurrentCalendarDate(selectedDate)
                _calendarList = getMonthList(selectedDate)
            }
            getDailyAccount(historyList)
            _isLoading.value = false
            _calendarList
        }
    }

    private fun getMonthList(selectedMonth: LocalDate) : List<CalendarItem> {
        val list = mutableListOf<CalendarItem>()
        val firstDayOfMonth = selectedMonth.withDayOfMonth(1)
        startMonthIndex = getPrevOffSet(firstDayOfMonth)
        val startValue = firstDayOfMonth.minusDays(startMonthIndex.toLong())
        for (i in 0 until TOTAL_DAY) {
            val date = startValue.plusDays(i.toLong())
            list.add(CalendarItem(
                date = date,
                isSameMonth = isSameMonth(selectedMonth, date),
                isToday = isSameDay(date)
            ))
        }
        return list
    }

    private fun getPrevOffSet(date: LocalDate): Int {
        return (date.dayOfWeek.value) % 7
    }

    private fun getDailyAccount(historyList : List<HistoryItem>) {
        _calendarList.forEach {
            it.SumAmount = 0
            it.incomeAmount = 0
            it.spendAmount = 0
        }
        historyList.forEach { history ->
            val calendarIndex = getDayValue(history.time) + startMonthIndex - 1
            if (history.isSpend) {
                _calendarList[calendarIndex].SumAmount -= history.amount
                _calendarList[calendarIndex].spendAmount -= history.amount
            } else {
                _calendarList[calendarIndex].SumAmount += history.amount
                _calendarList[calendarIndex].incomeAmount += history.amount
            }
        }
    }

}