package com.psw9999.android_accountbook_18.ui.categoryadd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.psw9999.android_accountbook_18.databinding.ItemCategoryColorBinding

class CategoryColorAdapter(
    private val context: Context
) : BaseAdapter() {

    private var categoryColorList = listOf<Int>()
    private var selectedColorIndex = 0

    private var onColorClickListener : ((Int)->Unit)? = null

    fun setOnColorClickListener(listener: ((Int)->Unit)) {
        this.onColorClickListener = listener
    }

    fun setCategoryColorList(categoryColorList: List<Int>) {
        this.categoryColorList = categoryColorList
    }

    fun setSelectedColorIndex(selectedColorIndex : Int) {
        if(this.selectedColorIndex != selectedColorIndex) {
            this.selectedColorIndex = selectedColorIndex
            onColorClickListener?.invoke(selectedColorIndex)
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int = categoryColorList.size

    override fun getItem(position: Int): Int = categoryColorList[position]

    override fun getItemId(position: Int): Long = 0

    // TODO : 개선필요 -> 터치마다 그리드 뷰 내의 모든 뷰를 다시 그려서 비효율적..
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding =
            ItemCategoryColorBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        with(binding) {
            categoryColor.setBackgroundColor(getItem(position))
            if (position == selectedColorIndex) {
                categoryColor.scaleX = 1F
                categoryColor.scaleY = 1F
            } else {
                categoryColor.scaleX = 0.7F
                categoryColor.scaleX = 0.7F
            }
            categoryColor.setOnClickListener {
                setSelectedColorIndex(position)
            }
            return root
        }
    }
}