package com.psw9999.android_accountbook_18.ui.historyinput.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.ItemInputSpinnerBinding
import com.psw9999.android_accountbook_18.databinding.ItemInputSpinnerHeadBinding
import com.psw9999.android_accountbook_18.ui.historyinput.HistoryInputFragment.Companion.spinnerInitValue

class InputSpinnerAdapter(
    private val context: Context
) : BaseAdapter()  {

    private val spinnerAddMenu = Pair(0,context.getString(R.string.input_sp_add))
    private var spinnerList : MutableList<Pair<Int,String>> = mutableListOf(spinnerAddMenu)
    private var selectedValue : Pair<Int, String> = spinnerInitValue

    fun setSpinnerList(spinnerList : List<Pair<Int,String>>) {
        this.spinnerList.apply {
            this.clear()
            this.addAll(spinnerList)
            this.add(spinnerAddMenu)
        }
    }

    fun setSelectedValue(selectedValue : Pair<Int, String>) {
        this.selectedValue = selectedValue
        notifyDataSetChanged()
    }

    override fun getCount(): Int = spinnerList.size

    override fun getItem(position: Int): Pair<Int,String> = spinnerList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    /*
    데이터 바인딩 적용시 깜빡거림 현상이 있어 뷰바인딩 적용!
    */
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding =
            ItemInputSpinnerHeadBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        if (selectedValue.second.isEmpty()) {
            binding.tvSpinnerHead.text = context.getString(R.string.input_sp_hint)
            binding.tvSpinnerHead.setTextColor(ContextCompat.getColor(context, R.color.lightPurple))
        } else {
            binding.tvSpinnerHead.text = selectedValue.second
            binding.tvSpinnerHead.setTextColor(ContextCompat.getColor(context, R.color.purple))
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val binding = ItemInputSpinnerBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        binding.currentValue = spinnerList[position].second
        return binding.root
    }
}