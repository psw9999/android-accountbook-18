package com.psw9999.android_accountbook_18.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.psw9999.android_accountbook_18.data.model.StatisticsItem
import com.psw9999.android_accountbook_18.databinding.ItemStatisticsCategoryBinding

class StatisticsListAdapter
    : ListAdapter<StatisticsItem, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<StatisticsItem>() {
            override fun areItemsTheSame(
                oldItem: StatisticsItem,
                newItem: StatisticsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StatisticsItem,
                newItem: StatisticsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class StatisticsViewHolder(private val binding: ItemStatisticsCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(statisticsItem: StatisticsItem) {
            binding.statisticsItem = statisticsItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return StatisticsViewHolder(
            ItemStatisticsCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StatisticsViewHolder).binding(getItem(position))
    }

}