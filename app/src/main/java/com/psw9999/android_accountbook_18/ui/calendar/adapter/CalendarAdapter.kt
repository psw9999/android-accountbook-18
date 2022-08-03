package com.psw9999.android_accountbook_18.ui.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.model.CalendarItem
import com.psw9999.android_accountbook_18.databinding.ItemCalendarBinding

class CalendarAdapter(
    private val context : Context
) :
    ListAdapter<CalendarItem, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CalendarItem>() {
            override fun areItemsTheSame(
                oldItem: CalendarItem,
                newItem: CalendarItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CalendarItem,
                newItem: CalendarItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class CalendarViewHolder(private val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(calendarItem: CalendarItem) {
            binding.calendarItem = calendarItem
            when(calendarItem.date.dayOfWeek.value) {
                7 -> binding.tvCalendarDay.setTextColor(ContextCompat.getColor(context, R.color.sunday))
                6 -> binding.tvCalendarDay.setTextColor(ContextCompat.getColor(context, R.color.saturday))
                else -> binding.tvCalendarDay.setTextColor(ContextCompat.getColor(context, R.color.purple))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CalendarViewHolder(
            ItemCalendarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CalendarViewHolder).binding(getItem(position))
    }

}