package com.psw9999.android_accountbook_18.ui.categoryadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryAddViewModel : ViewModel() {

    private val _isSpend = MutableLiveData(false)
    val isSpend : LiveData<Boolean> = _isSpend

    private val _catergoryTitle = MutableLiveData("")
    val categoryTitle : LiveData<String> = _catergoryTitle

    private val _selectedColorIndex = MutableLiveData(0)
    val selectedColorIndex : LiveData<Int> = _selectedColorIndex

    private val _isRevising = MutableLiveData(false)
    val isRevising : LiveData<Boolean> = _isRevising

    private val _categoryId = MutableLiveData(0)
    val categoryId: LiveData<Int> = _categoryId

    var befColor : Int = 0

    fun setIsSpend(isSpend : Boolean) {
        _isSpend.value = isSpend
    }

    fun setCategoryTitle(categoryTitle : String) {
        _catergoryTitle.value = categoryTitle
    }

    fun setSelectedColorIndex(index : Int) {
        _selectedColorIndex.value = index
    }

    fun setIsRevising(isRevising: Boolean) {
        _isRevising.value = isRevising
    }

    fun setCategoryId(categoryId: Int) {
        _categoryId.value = categoryId
    }

}