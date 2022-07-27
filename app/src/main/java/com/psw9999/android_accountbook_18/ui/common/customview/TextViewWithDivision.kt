package com.psw9999.android_accountbook_18.ui.common.customview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.TextviewWithDivisionBinding

class TextViewWithDivision(
    context: Context,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    private val binding: TextviewWithDivisionBinding
            by lazy {
                TextviewWithDivisionBinding.inflate(
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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithDivision)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val title = typedArray.getText(R.styleable.TextViewWithDivision_setTitle)
        binding.tvTitle.text = title
    }

}