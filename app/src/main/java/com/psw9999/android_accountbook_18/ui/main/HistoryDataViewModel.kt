package com.psw9999.android_accountbook_18.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.HistoryDto
import com.psw9999.android_accountbook_18.data.repository.history.HistoryRepository
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import com.psw9999.android_accountbook_18.util.toast
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

    private val _histories = MutableStateFlow<List<HistoryItem>>(listOf())
    val histories: StateFlow<List<HistoryItem>> = _histories

    private val _selectedDate = MutableStateFlow<LocalDate>(currentDate)
    val selectedDate : StateFlow<LocalDate> = _selectedDate

    private val _incomeSum = MutableStateFlow<Int>(0)
    val incomeSum : StateFlow<Int> = _incomeSum

    private val _spendSum = MutableStateFlow<Int>(0)
    val spendSum : StateFlow<Int> = _spendSum

    private val _isLoading = MutableStateFlow(true)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _isComplete = MutableStateFlow(false)
    val isComplete : StateFlow<Boolean> = _isComplete

    fun setIsComplete(isComplete : Boolean) {
        _isComplete.value = isComplete
    }

    fun setIsLoading(isLoading : Boolean) {
        _isLoading.value = isLoading
    }

    fun getMonthHistories(year: Int, month: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getMonthHistorys(year, month).let { result ->
                _isLoading.value = false
                if (result is Result.Success) {
                    getAmountSum(result.data)
                    _histories.value = result.data
                } else {
                    toast("내역 로드에 실패하였습니다.")
                    _histories.value = arrayListOf()
                }
            }
        }
    }

    fun saveHistory(
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
            ).let { result ->
                if (result is Result.Success) {
                    getMonthHistories(selectedDate.value.year,selectedDate.value.monthValue)
                    _isComplete.value = true
                    toast("내역을 저장하였습니다.")
                } else {
                    toast("내역 저장에 실패하였습니다.")
                }
            }
        }
    }

    fun updateHistory(historyDto: HistoryDto) {
        viewModelScope.launch {
            repository.updateHistory(historyDto).let { result ->
                if (result is Result.Success) {
                    getMonthHistories(selectedDate.value.year,selectedDate.value.monthValue)
                    _isComplete.value = true
                    toast("내역을 업데이트하였습니다.")
                } else {
                    toast("내역 업데이트에 실패하였습니다.")
                }
            }
        }
    }

    fun deleteHistories(idList : List<Int>) {
        viewModelScope.launch {
            repository.deleteHistories(idList).let { result ->
                if (result is Result.Success) {
                    getMonthHistories(selectedDate.value.year,selectedDate.value.monthValue)
                    _isComplete.value = true
                    toast("내역을 삭제하였습니다.")
                } else {
                    toast("내역 삭제에 실패하였습니다.")
                }
            }
        }
    }

    fun setSelectedDate(year : Int, month : Int) {
        _selectedDate.value = LocalDate.of(year, month,1)
        viewModelScope.launch {
            getMonthHistories(_selectedDate.value.year, _selectedDate.value.monthValue)
        }
    }

    fun setPreviousMonth() {
        _selectedDate.value = _selectedDate.value.minusMonths(1)
        viewModelScope.launch {
            getMonthHistories(_selectedDate.value.year, _selectedDate.value.monthValue)
        }
    }

    fun setNextMonth() {
        _selectedDate.value = _selectedDate.value.plusMonths(1)
        viewModelScope.launch {
            getMonthHistories(_selectedDate.value.year, _selectedDate.value.monthValue)
        }
    }

    private fun getAmountSum(historyItem : List<HistoryItem>) {
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