package com.psw9999.android_accountbook_18.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psw9999.android_accountbook_18.R

class MainViewModel : ViewModel() {

    private val _currentMenu = MutableLiveData(MainBottomMenuType.HISTORY)
    val currentMenu: LiveData<MainBottomMenuType> = _currentMenu

    private fun getBottomMenuType(itemId: Int): MainBottomMenuType =
        when (itemId) {
            R.id.bottom_menu_history -> MainBottomMenuType.HISTORY
            R.id.bottom_menu_calendar -> MainBottomMenuType.CALENDAR
            R.id.bottom_menu_statistics -> MainBottomMenuType.STATISTICS
            R.id.bottom_menu_configuration -> MainBottomMenuType.CONFIGURATION
            else -> throw IllegalArgumentException()
        }

    fun setCurrentMenu(itemId: Int): Boolean {
        changeCurrentMenu(getBottomMenuType(itemId))
        return true
    }

    private fun changeCurrentMenu(bottomMenuType: MainBottomMenuType) {
        if (_currentMenu.value != bottomMenuType) _currentMenu.value = bottomMenuType
    }

}