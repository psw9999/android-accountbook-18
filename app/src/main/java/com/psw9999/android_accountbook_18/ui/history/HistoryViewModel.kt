package com.psw9999.android_accountbook_18.ui.history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryViewModel : ViewModel() {

    // TODO : StateFlow 리스트 아이템 변화 감지할 수 있는 법 확인해보기
//    private val _filterTab = MutableStateFlow<MutableList<Boolean>>(mutableListOf(true, false))
//    val filterTab : StateFlow<MutableList<Boolean>> = _filterTab
//
//    fun setFilterTab(index : Int) {
//        var filterList = _filterTab.value
//        filterList[index] = !filterList[index]
//        _filterTab.value = _filterTab.value
//    }

    private val _isIncomeEnabled = MutableStateFlow<Boolean>(true)
    val isIncomeEnabled : StateFlow<Boolean> = _isIncomeEnabled

    private val _isSpendEnabled = MutableStateFlow<Boolean>(false)
    val isSpendEnabled : StateFlow<Boolean> = _isSpendEnabled

    fun setIsIncomeEnabled(isEnabled : Boolean) {
        this._isIncomeEnabled.value = isEnabled
    }

    fun setIsSpendEnabled(isEnabled : Boolean) {
        this._isSpendEnabled.value = isEnabled
    }

}