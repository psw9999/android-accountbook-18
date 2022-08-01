package com.psw9999.android_accountbook_18.extension

import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.psw9999.android_accountbook_18.R

@BindingAdapter("app:checkTab")
fun MaterialButtonToggleGroup.checkTab(isSpend : Boolean) {
    if(isSpend) this.check(R.id.tbtn_spend)
    else this.check(R.id.tbtn_income)
}