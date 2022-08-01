package com.psw9999.android_accountbook_18.ui.history

import android.util.Log
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.data.model.HistoryListItem
import com.psw9999.android_accountbook_18.databinding.FragmentHistoryBinding
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.history.adapter.HistoryListAdapter
import com.psw9999.android_accountbook_18.ui.historyinput.HistoryInputFragment
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

    private val historyDataViewModel : HistoryDataViewModel by viewModels()
    private val historyViewModel : HistoryViewModel by viewModels()
    private val historyListAdapter : HistoryListAdapter by lazy {HistoryListAdapter()}

    override fun observe() {
        getHistoryList()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    historyDataViewModel.historys,
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
        binding.viewModel = historyDataViewModel
        binding.rvHistory.adapter = historyListAdapter
        // TODO : 프래그먼트 매니저 구조 수정 예정
        binding.fbtnHistoryAdd.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(R.id.l_main_container, HistoryInputFragment())
            transaction!!.addToBackStack(null)
            transaction.hide(this)
            transaction.commit()
        }

        binding.tbtngTypeSelector.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when(checkedId) {
                R.id.tbtn_spend -> historyViewModel.setIsSpendEnabled(isChecked)
                R.id.tbtn_income -> historyViewModel.setIsIncomeEnabled(isChecked)
            }
        }
    }

    private fun getHistoryList() {
        historyDataViewModel
        CoroutineScope(Dispatchers.IO).launch {
            historyDataViewModel.getMonthHistorys(
                historyDataViewModel.selectedDate.value.year, historyDataViewModel.selectedDate.value.monthValue
            )
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