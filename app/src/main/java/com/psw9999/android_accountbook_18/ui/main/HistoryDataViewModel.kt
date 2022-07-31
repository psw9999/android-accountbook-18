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
class HistoryDataViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _historys = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historys: StateFlow<List<HistoryItem>> = _historys

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
                    _historys.value = result.data
                    getAmountSum(result.data)
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

    fun getAmountSum(historyItem : List<HistoryItem>) {
        var spendSum = 0
        var incomeSum = 0
        historyItem.forEach {
            if(it.isSpend) spendSum += it.amount
            else incomeSum += it.amount
        }
        _spendSum.value = spendSum
        _incomeSum.value = incomeSum
    }
}