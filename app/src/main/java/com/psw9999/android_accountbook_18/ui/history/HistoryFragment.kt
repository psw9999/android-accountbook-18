package com.psw9999.android_accountbook_18.ui.history

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.model.HistoryListItem
import com.psw9999.android_accountbook_18.databinding.FragmentHistoryBinding
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.history.adapter.HistoryListAdapter
import com.psw9999.android_accountbook_18.ui.historyinput.HistoryInputFragment
import com.psw9999.android_accountbook_18.ui.main.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history){

    private val historyViewModel : HistoryViewModel by viewModels()
    private val historyListAdapter : HistoryListAdapter by lazy {HistoryListAdapter()}

    override fun observe() {
        getHistoryList()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                historyViewModel.historys.collectLatest {
                    if (it.isEmpty()) historyListAdapter.submitList(listOf(HistoryListItem.HistoryEmpty))
                    else historyListAdapter.submitList(it)
                }
            }
        }
    }

    override fun initViews() {
        binding.viewModel = historyViewModel
        binding.rvHistory.adapter = historyListAdapter
        // TODO : 프래그먼트 매니저 구조 수정 예정
        binding.fbtnHistoryAdd.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()?.add(R.id.l_main_container, HistoryInputFragment())
            transaction!!.addToBackStack(null)
            transaction.hide(this)
            transaction.commit()
        }
    }

    private fun getHistoryList() {
        historyViewModel
        CoroutineScope(Dispatchers.IO).launch {
            historyViewModel.getMonthHistorys(
                historyViewModel.selectedDate.value.year, historyViewModel.selectedDate.value.monthValue
            )
        }
    }
}