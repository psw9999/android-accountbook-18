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
import com.psw9999.android_accountbook_18.data.model.CalendarListItem
import com.psw9999.android_accountbook_18.databinding.ItemCalendarBinding
import com.psw9999.android_accountbook_18.databinding.ItemEmpryViewBinding

class CalendarAdapter(
    private val context : Context
) : ListAdapter<CalendarListItem, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CalendarListItem>() {
            override fun areItemsTheSame(
                oldItem: CalendarListItem,
                newItem: CalendarListItem
            ): Boolean {
                if (oldItem is CalendarListItem.CalendarContent && newItem is CalendarListItem.CalendarContent) {
                    return (oldItem.calendarItem.date == newItem.calendarItem.date) && (oldItem.calendarItem.spendAmount == newItem.calendarItem.spendAmount) &&
                            (oldItem.calendarItem.incomeAmount == newItem.calendarItem.incomeAmount)
                }
                return false
            }

            override fun areContentsTheSame(
                oldItem: CalendarListItem,
                newItem: CalendarListItem
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

    class CalendarLoadingViewHolder(private val binding: ItemEmpryViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CalendarListItem.CalendarContent -> R.layout.item_calendar
            is CalendarListItem.CalendarLoading -> R.layout.item_empry_view
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_calendar -> CalendarViewHolder(
                ItemCalendarBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_empry_view -> CalendarLoadingViewHolder(
                ItemEmpryViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let { calendarListItem ->
            when (calendarListItem) {
                is CalendarListItem.CalendarContent -> (holder as CalendarViewHolder).binding(
                    calendarListItem.calendarItem
                )
                is CalendarListItem.CalendarLoading -> (holder as CalendarLoadingViewHolder)
            }
        }
    }
}