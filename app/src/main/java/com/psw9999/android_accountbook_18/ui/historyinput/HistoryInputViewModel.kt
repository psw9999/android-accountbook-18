package com.psw9999.android_accountbook_18.ui.historyinput

import androidx.lifecycle.ViewModel
import com.psw9999.android_accountbook_18.ui.historyinput.HistoryInputFragment.Companion.spinnerInitValue
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import com.psw9999.android_accountbook_18.util.DateUtil.dateFormat
import com.psw9999.android_accountbook_18.util.DateUtil.dateToHistoryInputDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class HistoryInputViewModel : ViewModel() {

    private val _historyDate = MutableStateFlow(
        String.format(
            dateFormat,
            currentDate.year,
            currentDate.monthValue,
            currentDate.dayOfMonth))
    val historyDate : StateFlow<String> = _historyDate

    private val _displayHistoryDate = MutableStateFlow(dateToHistoryInputDate(_historyDate.value))
    val displayHistoryDate : StateFlow<String> = _displayHistoryDate

    private val _historyId = MutableStateFlow(-1)
    val historyId : StateFlow<Int> = _historyId

    private val _paymentMethod = MutableStateFlow(spinnerInitValue)
    val paymentMethod : StateFlow<Pair<Int,String>> = _paymentMethod

    private val _catergory = MutableStateFlow(spinnerInitValue)
    val catergory : StateFlow<Pair<Int,String>> = _catergory

    private val _amount = MutableStateFlow("")
    val amount : StateFlow<String> = _amount

    private val _isSpend = MutableStateFlow(false)
    val isSpend : StateFlow<Boolean> = _isSpend

    private val _content = MutableStateFlow("")
    val content : StateFlow<String> = _content

    private val _isRegisterEnabled = MutableStateFlow(false)
    val isRegisterEnabled : StateFlow<Boolean> = _isRegisterEnabled

    private val _isRevised = MutableStateFlow(false)
    val isRevised : StateFlow<Boolean> = _isRevised

    val stateCombine =
        combine(
            historyDate,
            amount,
            isSpend,
            paymentMethod
        ) { historyDate, amount, isSpend, paymentMethod ->
            historyDate.isNotEmpty() && amount.isNotEmpty() && ((!isSpend) || paymentMethod.second.isNotEmpty())
        }

    fun setHistoryId(id : Int) {
        _historyId.value = id
    }

    fun setHistoryDate(year: Int, month: Int, dayOfMonth: Int) {
        _historyDate.value = String.format(dateFormat, year, month, dayOfMonth)
        _displayHistoryDate.value = dateToHistoryInputDate(_historyDate.value)
    }

    fun setHistoryDate(time : String) {
        _historyDate.value = time
    }

    fun setPaymentMethod(method : Pair<Int, String>) {
        _paymentMethod.value = method
    }

    fun setCategory(category : Pair<Int, String>) {
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

    fun setIsRevised(isRevised : Boolean) {
        _isRevised.value = isRevised
    }
}