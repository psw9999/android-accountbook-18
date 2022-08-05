package com.psw9999.android_accountbook_18.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
import com.psw9999.android_accountbook_18.data.repository.category.CategoryRepository
import com.psw9999.android_accountbook_18.util.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categorys = MutableStateFlow<List<CategoryDto>>(emptyList())
    val category : StateFlow<List<CategoryDto>> = _categorys

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _isComplete = MutableStateFlow(false)
    val isComplete : StateFlow<Boolean> = _isComplete

    fun setIsComplete(isComplete : Boolean) {
        _isComplete.value = isComplete
    }

    fun getAllCategory() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getAllCategorys().let { result ->
                if (result is Result.Success) {
                    _categorys.value = result.data
                } else {
                    toast("카테고리 로드에 실패하였습니다.")
                    _categorys.value = emptyList()
                }
                _isLoading.value = false
            }
        }
    }

    fun saveCategory(isSpend : Boolean, title : String, color : Int) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.saveCategory(isSpend, title, color).let { result ->
                if (result is Result.Success) {
                    getAllCategory()
                    _isComplete.value = true
                    toast("카테고리를 저장하였습니다.")
                } else {
                    toast("카테고리 저장에 실패하였습니다.")
                }
                _isLoading.value = false
            }
        }
    }

    fun updatePayment(categoryDto: CategoryDto) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.updateCategory(categoryDto).let { result ->
                if (result is Result.Success) {
                    getAllCategory()
                    _isComplete.value = true
                    toast("카테고리를 수정하였습니다.")
                } else {
                    toast("카테고리 수정에 실패하였습니다.")
                }
                _isLoading.value = false
            }
        }
    }

}