package com.psw9999.android_accountbook_18.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.repository.history.HistoryRepository
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.data.model.HistoryListItem
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import com.psw9999.android_accountbook_18.util.DateUtil.dateToHistoryHeaderDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _historys = MutableStateFlow<ArrayList<HistoryListItem>>(arrayListOf())
    val historys: StateFlow<ArrayList<HistoryListItem>> = _historys

    private val _selectedDate = MutableStateFlow<LocalDate>(currentDate)
    val selectedDate : StateFlow<LocalDate> = _selectedDate

    private val _incomeSum = MutableStateFlow<Int>(0)
    val incomeSum : StateFlow<Int> = _incomeSum

    private val _spendSum = MutableStateFlow<Int>(0)
    val spendSum : StateFlow<Int> = _spendSum

    suspend fun getMonthHistorys(year: Int, month: Int) {
        viewModelScope.launch {
            repository.getMonthHistorys(year, month).let { result ->
                if (result is Result.Success) {
                    _historys.value = mapperHistoryListItem(result.data)
                } else {
                    // TODO : DB Read 실패 UI에 보여주기
                    Log.e("error","${result}")
                    _historys.value = arrayListOf()
                }
            }
        }
    }

    suspend fun saveHistory(
        time: String,
        amount: Int,
        content: String,
        paymentId: Int?,
        categoryId: Int
    ) {
        viewModelScope.launch {
            repository.saveHistory(
                time = time,
                amount = amount,
                content = content,
                paymentId = paymentId,
                categoryId = categoryId
            )
        }
    }

    private fun mapperHistoryListItem(historyItem : List<HistoryItem>) : ArrayList<HistoryListItem> {
        var befHeader = Pair(0, "")
        var incomeSum = 0
        var spendSum = 0
        val result = arrayListOf<HistoryListItem>()

        for ((index, history) in historyItem.withIndex()) {
            if (befHeader.second != history.time) {
                val historyHeaderDate = dateToHistoryHeaderDate(history.time)
                if(history.isSpend) result.add(HistoryListItem.HistoryHeader(historyHeaderDate, history.amount,0))
                else result.add(HistoryListItem.HistoryHeader(historyHeaderDate, 0, history.amount))
                befHeader = Pair(result.size-1, history.time)
            } else {
                if (history.isSpend) (result[befHeader.first] as HistoryListItem.HistoryHeader).spend += history.amount
                else (result[befHeader.first] as HistoryListItem.HistoryHeader).income += history.amount
            }

            if (index == (historyItem.size-1) || history.time != historyItem[index+1].time) {
                history.isLast = true
            }

            if (history.isSpend) spendSum += history.amount
            else incomeSum += history.amount
            result.add(HistoryListItem.HistoryContent(history))
        }
        _spendSum.value = spendSum
        _incomeSum.value = incomeSum
        return result
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setPreviousMonth() {
        _selectedDate.value = _selectedDate.value.minusMonths(1)
        viewModelScope.launch {
            getMonthHistorys(_selectedDate.value.year, _selectedDate.value.monthValue)
        }
    }

    fun setNextMonth() {
        _selectedDate.value = _selectedDate.value.plusMonths(1)
        viewModelScope.launch {
            getMonthHistorys(_selectedDate.value.year, _selectedDate.value.monthValue)
        }
    }
}