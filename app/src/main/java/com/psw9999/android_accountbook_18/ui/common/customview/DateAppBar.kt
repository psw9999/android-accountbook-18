package com.psw9999.android_accountbook_18.ui.common.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.DateAppBarBinding
import com.psw9999.android_accountbook_18.util.DateUtil.dateAppbarFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateAppBar(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    private val binding: DateAppBarBinding by lazy {
        DateAppBarBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )
    }

    init {
        getAttrs(attrs)
        addView(binding.root)
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DateAppbar)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val title = typedArray.getText(R.styleable.DateAppbar_setDate)
        binding.tvDate.text = title
    }

    fun setDate(date : LocalDate) {
        binding.tvDate.text = date.format(DateTimeFormatter.ofPattern(dateAppbarFormat))
    }

    fun setOnLeftBtnClickListener(listener: OnClickListener) {
        binding.ivLeftBtn.setOnClickListener(listener)
    }

    fun setOnTitleClickListener(listener: OnClickListener) {
        binding.tvDate.setOnClickListener(listener)
    }

    fun setOnRightBtnClickListener(listener: OnClickListener) {
        binding.ivRightBtn.setOnClickListener(listener)
    }

}