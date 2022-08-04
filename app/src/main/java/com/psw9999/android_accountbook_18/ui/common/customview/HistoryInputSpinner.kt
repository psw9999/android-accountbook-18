package com.psw9999.android_accountbook_18.ui.common.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class HistoryInputSpinner(
    context: Context, attrs: AttributeSet? = null
) : AppCompatSpinner(context, attrs) {

    private var listener : OnItemSelectedListener? = null

    private var onDropDownEventsListener: OnDropDownEventsListener? = null
    private var isDropDownOpened: Boolean = false

    override fun setSelection(position: Int) {
        super.setSelection(position)
        if (position == selectedItemPosition) {
            listener!!.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }

    fun setOnDropDownListener(listener: OnDropDownEventsListener) {
        onDropDownEventsListener = listener
    }

    interface OnDropDownEventsListener {
        fun dropDownOpen()
        fun dropDownClose()
    }

    override fun performClick(): Boolean {
        isDropDownOpened = true
        onDropDownEventsListener?.dropDownOpen()
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (isDropDownOpened && hasWindowFocus) {
            performClosedEvent()
        }
    }

    private fun performClosedEvent() {
        isDropDownOpened = false
        onDropDownEventsListener?.dropDownClose()
    }

}