package com.psw9999.android_accountbook_18.ui.history

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.data.model.HistoryListItem
import com.psw9999.android_accountbook_18.databinding.FragmentHistoryBinding
import com.psw9999.android_accountbook_18.ui.bottomsheet.DateBottomSheet
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.history.adapter.HistoryListAdapter
import com.psw9999.android_accountbook_18.ui.historyinput.HistoryInputFragment
import com.psw9999.android_accountbook_18.ui.historyinput.HistoryInputFragment.Companion.HISTORY_ITEM
import com.psw9999.android_accountbook_18.ui.main.HistoryDataViewModel
import com.psw9999.android_accountbook_18.util.DateUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history){

    private val historyDataViewModel : HistoryDataViewModel by activityViewModels()
    private val historyViewModel : HistoryViewModel by viewModels()
    private val historyListAdapter : HistoryListAdapter by lazy {HistoryListAdapter()}

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    historyDataViewModel.histories,
                    historyViewModel.isIncomeEnabled,
                    historyViewModel.isSpendEnabled
                ) { historys, isIncomeEnabled, isSpendEnabled ->
                    historys.filter { historyItem ->
                        if (historyItem.isSpend) isSpendEnabled
                        else isIncomeEnabled
                    }.mapperHistoryListItem()
                }.collectLatest {
                    if (it.isEmpty()) historyListAdapter.submitList(listOf(HistoryListItem.HistoryEmpty))
                    else historyListAdapter.submitList(it)
                }
            }
        }
    }



    override fun initViews() {
        binding.historyDataViewModel = historyDataViewModel
        binding.historyViewModel = historyViewModel
        binding.rvHistory.adapter = historyListAdapter

        binding.fbtnHistoryAdd.setOnClickListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.l_main_container, HistoryInputFragment(), "History")
            transaction.hide(this)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        historyListAdapter.setOnHistoryClickListener { historyItem ->
            if (historyViewModel.isDeleteMode.value) {
                historyItem.isSelected = !historyItem.isSelected
                if (historyItem.isSelected) historyViewModel.addDeleteList(historyItem.id)
                else historyViewModel.removeDeleteList(historyItem.id)

                if (historyViewModel.deleteIdList.value.isEmpty()) historyViewModel.setIsDeleteMode(false)
            }

            else {
                val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(R.id.l_main_container,
                    HistoryInputFragment().apply{
                        arguments = Bundle().apply {
                            putParcelable(HISTORY_ITEM, historyItem)
                        }
                    })
                transaction!!.addToBackStack(null)
                transaction.hide(this)
                transaction.commit()
            }
        }

        historyListAdapter.setOnHistoryLongClickListener { historyItem ->
            if (!historyViewModel.isDeleteMode.value) {
                historyViewModel.setIsDeleteMode(true)
                historyViewModel.addDeleteList(historyItem.id)
                historyItem.isSelected = true
            }
        }

        binding.tbtngTypeSelector.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when(checkedId) {
                R.id.tbtn_spend -> historyViewModel.setIsSpendEnabled(isChecked)
                R.id.tbtn_income -> historyViewModel.setIsIncomeEnabled(isChecked)
            }
        }

        binding.abDeleteMode.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.history_appbar_trash -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        historyDataViewModel.deleteHistories(historyViewModel.deleteIdList.value)
                        historyDataViewModel.getMonthHistorys(
                            historyDataViewModel.selectedDate.value.year,
                            historyDataViewModel.selectedDate.value.monthValue
                        )
                        historyViewModel.setIsDeleteMode(false)
                    }
                    true
                }
                else -> true
            }
        }
        initBottomSheet()
    }

    private fun initBottomSheet() {
        binding.abHistoryDate.setOnTitleClickListener {
            val bottomSheet = DateBottomSheet()
            bottomSheet.show(childFragmentManager, "dateBottomSheet")
        }
    }

    private fun List<HistoryItem>.mapperHistoryListItem() : ArrayList<HistoryListItem> {
        var befHeader = Pair(0, "")
        val result = arrayListOf<HistoryListItem>()

        for ((index, history) in this.withIndex()) {
            if (befHeader.second != history.time) {
                val historyHeaderDate = DateUtil.dateToHistoryHeaderDate(history.time)
                if(history.isSpend) result.add(HistoryListItem.HistoryHeader(historyHeaderDate, history.amount,0))
                else result.add(HistoryListItem.HistoryHeader(historyHeaderDate, 0, history.amount))
                befHeader = Pair(result.size-1, history.time)
            } else {
                if (history.isSpend) (result[befHeader.first] as HistoryListItem.HistoryHeader).spend += history.amount
                else (result[befHeader.first] as HistoryListItem.HistoryHeader).income += history.amount
            }

            if (index == (this.size-1) || history.time != this[index+1].time) {
                history.isLast = true
            }
            result.add(HistoryListItem.HistoryContent(history))
        }
        return result
    }


}