package com.psw9999.android_accountbook_18.extension

import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.psw9999.android_accountbook_18.ui.main.MainViewModel

// TODO : 람다로 대체하는 법 확인해보기
@BindingAdapter("app:menuOnClick")
fun BottomNavigationView.menuOnClick(viewModel: MainViewModel) {
    this.setOnItemSelectedListener { menuItem ->
        viewModel.setCurrentMenu(menuItem.itemId)
    }
}