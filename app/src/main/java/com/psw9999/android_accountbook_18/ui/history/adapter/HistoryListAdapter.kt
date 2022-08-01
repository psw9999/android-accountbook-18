package com.psw9999.android_accountbook_18.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.data.model.HistoryListItem
import com.psw9999.android_accountbook_18.databinding.ItemHistoryContentBinding
import com.psw9999.android_accountbook_18.databinding.ItemHistoryEmptyBinding
import com.psw9999.android_accountbook_18.databinding.ItemHistoryHeaderBinding

class HistoryListAdapter
    : ListAdapter<HistoryListItem, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HistoryListItem>() {
            override fun areItemsTheSame(
                oldItem: HistoryListItem,
                newItem: HistoryListItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryListItem,
                newItem: HistoryListItem
            ): Boolean {
                return if (oldItem is HistoryListItem.HistoryHeader && newItem is HistoryListItem.HistoryHeader) {
                    ((oldItem.date == newItem.date) && (oldItem.spend == newItem.spend) && (oldItem.income == newItem.income))
                } else if (oldItem is HistoryListItem.HistoryContent && newItem is HistoryListItem.HistoryContent) {
                    (oldItem.history == newItem.history)
                } else
                    false
            }
        }
    }

    private var onHistoryClickListener : ((HistoryItem) -> Unit)? = null

    fun setOnHistoryClickListener(listener : (HistoryItem)->Unit) {
        this.onHistoryClickListener = listener
    }

    class HistoryHeaderViewHolder(private val binding: ItemHistoryHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(historyHeader: HistoryListItem.HistoryHeader) {
            binding.historyHeader = historyHeader
        }
    }

    class HistoryContentViewHolder(
        private val binding: ItemHistoryContentBinding,
        private val listener: ((HistoryItem) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(historyContent: HistoryListItem.HistoryContent) {
            binding.historyContent = historyContent.history
            binding.root.setOnClickListener {
                listener?.invoke(historyContent.history)
            }
        }
    }

    class HistoryEmptyViewHolder(private val binding : ItemHistoryEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HistoryListItem.HistoryHeader -> R.layout.item_history_header
            is HistoryListItem.HistoryContent -> R.layout.item_history_content
            is HistoryListItem.HistoryEmpty -> R.layout.item_history_empty
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_history_header -> HistoryHeaderViewHolder(
                ItemHistoryHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_history_content -> HistoryContentViewHolder(
                ItemHistoryContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onHistoryClickListener
            )
            R.layout.item_history_empty -> HistoryEmptyViewHolder(
                ItemHistoryEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let { historyListItem ->
            when(historyListItem) {
                is HistoryListItem.HistoryHeader -> (holder as HistoryHeaderViewHolder).binding(
                    historyListItem
                )
                is HistoryListItem.HistoryContent -> (holder as HistoryContentViewHolder).binding(
                    historyListItem
                )
                is HistoryListItem.HistoryEmpty -> (holder as HistoryEmptyViewHolder)
            }
        }
    }
}