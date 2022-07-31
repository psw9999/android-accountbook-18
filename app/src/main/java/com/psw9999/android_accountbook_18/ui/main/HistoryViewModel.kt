package com.psw9999.android_accountbook_18.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.repository.history.HistoryRepository
import com.psw9999.android_accountbook_18.data.vo.HistoryVo
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
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

    private val _historys = MutableStateFlow<List<HistoryVo>>(emptyList())
    val historys: StateFlow<List<HistoryVo>> = _historys

    private val _selectedDate = MutableStateFlow<LocalDate>(currentDate)
    val selectedDate : StateFlow<LocalDate> = _selectedDate

    suspend fun getMonthHistorys(year: Int, month: Int) {
        viewModelScope.launch {
            repository.getMonthHistorys(year, month).let { result ->
                if (result is Result.Success) {
                    _historys.value = result.data
                } else {
                    // TODO : DB Read 실패 UI에 보여주기
                    Log.e("error","${result}")
                    _historys.value = emptyList()
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
}