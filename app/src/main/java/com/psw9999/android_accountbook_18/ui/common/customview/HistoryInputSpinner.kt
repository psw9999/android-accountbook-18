package com.psw9999.android_accountbook_18.ui.common.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class HistoryInputSpinner(
    context: Context, attrs: AttributeSet? = null
) : AppCompatSpinner(context, attrs) {

    private var listener : OnItemSelectedListener? = null

    override fun setSelection(position: Int) {
        super.setSelection(position)
        if (position == selectedItemPosition) {
            listener!!.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }

}