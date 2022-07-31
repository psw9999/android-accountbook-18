package com.psw9999.android_accountbook_18.extension

import android.view.View
import androidx.databinding.BindingAdapter
import com.psw9999.android_accountbook_18.ui.common.customview.DateAppBar
import java.time.LocalDate

@BindingAdapter("app:setDate")
fun DateAppBar.setDate(date : LocalDate) {
    this.setDate(date)
}

@BindingAdapter("app:onPreBtnClick")
fun DateAppBar.onPreBtnClick(listener: View.OnClickListener) {
    this.setOnLeftBtnClickListener(listener)
}

@BindingAdapter("app:onNextBtnClick")
fun DateAppBar.onNextBtnClick(listener: View.OnClickListener) {
    this.setOnRightBtnClickListener(listener)
}

@BindingAdapter("app:onDateClick")
fun DateAppBar.onDateClick(listener: View.OnClickListener) {
    this.setOnTitleClickListener(listener)
}
