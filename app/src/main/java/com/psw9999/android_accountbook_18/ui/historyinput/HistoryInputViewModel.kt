package com.psw9999.android_accountbook_18.ui.historyinput

import androidx.lifecycle.ViewModel
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import com.psw9999.android_accountbook_18.util.DateUtil.dateFormat
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class HistoryInputViewModel : ViewModel() {

    private val _historyDate = MutableStateFlow(
        String.format(
            dateFormat,
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH) + 1,
            currentDate.get(Calendar.DAY_OF_MONTH)))
    val historyDate = _historyDate

    private val _paymentMethod = MutableStateFlow("")
    val paymentMethod = _paymentMethod

    private val _catergory = MutableStateFlow("")
    val catergory = _catergory

    private val _isSpend = MutableStateFlow(false)
    val isSpend = _isSpend

    fun setHistoryDate(year: Int, month: Int, dayOfMonth: Int) {
        historyDate.value = String.format(dateFormat, year, month + 1, dayOfMonth)
    }

    fun setPaymentMethod(method : String) {
        _paymentMethod.value = method
    }

    fun setCategory(category : String) {
        _catergory.value = category
    }

    fun setIsSpend(isSpend : Boolean) {
        _isSpend.value = isSpend
    }

}