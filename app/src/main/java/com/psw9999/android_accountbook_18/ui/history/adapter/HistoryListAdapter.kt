package com.psw9999.android_accountbook_18.ui.history.adapter

import android.view.LayoutInflater
import android.view.View
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
import com.psw9999.android_accountbook_18.databinding.ItemHistoryLoadingBinding

class HistoryListAdapter
    : ListAdapter<HistoryListItem, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HistoryListItem>() {
            override fun areItemsTheSame(
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

            override fun areContentsTheSame(
                oldItem: HistoryListItem,
                newItem: HistoryListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onHistoryClickListener : ((HistoryItem) -> Unit)? = null
    private var onHistoryLongClickListener : ((HistoryItem) -> Unit)? = null

    fun setOnHistoryClickListener(listener : (HistoryItem)->Unit) {
        this.onHistoryClickListener = listener
    }

    fun setOnHistoryLongClickListener(listener: (HistoryItem) -> Unit) {
        this.onHistoryLongClickListener = listener
    }

    fun doUncheck() {
        this.currentList.forEach {
            if (it is HistoryListItem.HistoryContent) {
                it.history.isSelected = false
            }
        }
        notifyDataSetChanged()
    }

    class HistoryHeaderViewHolder(private val binding: ItemHistoryHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(historyHeader: HistoryListItem.HistoryHeader) {
            binding.historyHeader = historyHeader
        }
    }

    inner class HistoryContentViewHolder(
        private val binding: ItemHistoryContentBinding,
        private val onClicklistener: ((HistoryItem) -> Unit)?,
        private val onLongClickListener: ((HistoryItem) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(historyContent: HistoryListItem.HistoryContent) {
            binding.historyContent = historyContent.history
            binding.root.setOnClickListener {
                onClicklistener?.invoke(historyContent.history)
                notifyItemChanged(adapterPosition)
            }

            binding.root.setOnLongClickListener {
                onLongClickListener?.invoke(historyContent.history)
                notifyItemChanged(adapterPosition)
                true
            }
        }
    }

    class HistoryEmptyViewHolder(private val binding : ItemHistoryEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)

    class HistoryLoadingViewHolder(private val binding : ItemHistoryLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HistoryListItem.HistoryHeader -> R.layout.item_history_header
            is HistoryListItem.HistoryContent -> R.layout.item_history_content
            is HistoryListItem.HistoryEmpty -> R.layout.item_history_empty
            is HistoryListItem.HistoryLoading -> R.layout.item_history_loading
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
                ), onHistoryClickListener, onHistoryLongClickListener
            )
            R.layout.item_history_empty -> HistoryEmptyViewHolder(
                ItemHistoryEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.item_history_loading -> HistoryLoadingViewHolder(
                ItemHistoryLoadingBinding.inflate(
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
                is HistoryListItem.HistoryLoading -> (holder as HistoryLoadingViewHolder)
            }
        }
    }
}