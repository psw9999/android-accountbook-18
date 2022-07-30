package com.psw9999.android_accountbook_18.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psw9999.android_accountbook_18.data.Result
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
import com.psw9999.android_accountbook_18.data.repository.category.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    // TODO : DTO -> Entity로 변경하여 적용
    private val _categorys = MutableStateFlow<List<CategoryDto>>(emptyList())
    val category : StateFlow<List<CategoryDto>> = _categorys

    suspend fun getAllCategory() {
        viewModelScope.launch {
            repository.getAllCategorys().let { result ->
                if (result is Result.Success) {
                    _categorys.value = result.data
                } else {
                    // TODO : DB Read 실패 UI에 보여주기
                    _categorys.value = emptyList()
                }
            }
        }
    }

    suspend fun saveCategory(isSpend : Boolean, title : String, color : Int) {
        viewModelScope.launch {
            repository.saveCategory(isSpend, title, color)
        }
    }

}