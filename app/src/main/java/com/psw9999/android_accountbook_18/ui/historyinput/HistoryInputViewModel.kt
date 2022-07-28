package com.psw9999.android_accountbook_18.ui.historyinput

import androidx.lifecycle.ViewModel
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import com.psw9999.android_accountbook_18.util.DateUtil.dateFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    val paymentMethod : StateFlow<String> = _paymentMethod

    private val _catergory = MutableStateFlow("")
    val catergory : StateFlow<String> = _catergory

    private val _amount = MutableStateFlow("")
    val amount : StateFlow<String> = _amount

    private val _isSpend = MutableStateFlow(false)
    val isSpend : StateFlow<Boolean> = _isSpend

    private val _content = MutableStateFlow("")
    val content : StateFlow<String> = _content

    private val _isRegisterEnabled  = MutableStateFlow(false)
    val isRegisterEnabled : StateFlow<Boolean> = _isRegisterEnabled

    val stateCombine =
        combine(
            historyDate,
            amount,
            isSpend,
            paymentMethod
        ) { historyDate, amount, isSpend, paymentMethod ->
            historyDate.isNotEmpty() && amount.isNotEmpty() && ((!isSpend) || paymentMethod.isNotEmpty())
        }

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

    fun setAmount(amount : String) {
        _amount.value = amount
    }

    fun setContent(content : String) {
        _content.value = content
    }

    fun setIsRegisterEnabled(isRegisterEnabled : Boolean) {
        _isRegisterEnabled.value = isRegisterEnabled
    }
}