package com.psw9999.android_accountbook_18.ui.calendar

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.FragmentCalendarBinding
import com.psw9999.android_accountbook_18.ui.bottomsheet.DateBottomSheet
import com.psw9999.android_accountbook_18.ui.calendar.adapter.CalendarAdapter
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.main.HistoryDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){

    private val historyDataViewModel : HistoryDataViewModel by activityViewModels()
    private val calendarViewModel : CalendarViewModel by viewModels()
    private val calendarAdapter : CalendarAdapter by lazy { CalendarAdapter(activityContext) }

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    historyDataViewModel.selectedDate.combine(historyDataViewModel.histories) { selectedDate, histories ->
                        Pair(selectedDate, histories)
                    }.collectLatest {
                        val result = calendarViewModel.getCalendarData(it.first, it.second)
                        calendarAdapter.submitList(result.await())
                    }
                }

                launch {
                    calendarViewModel.isLoading.collectLatest { isLoading ->
                        if (isLoading) {
                            binding.rvCalendar.visibility = View.INVISIBLE
                            binding.rvCalendarLoading.visibility = View.VISIBLE
                        } else {
                            binding.rvCalendar.visibility = View.VISIBLE
                            binding.rvCalendarLoading.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun initViews() {
        binding.historyDataViewModel = historyDataViewModel
        binding.rvCalendar.adapter = calendarAdapter
        binding.rvCalendar.itemAnimator = null
        binding.rvCalendar.setHasFixedSize(true)
        initBottomSheet()
    }

    private fun initBottomSheet() {
        binding.abCalendarDate.setOnTitleClickListener {
            val bottomSheet = DateBottomSheet()
            bottomSheet.show(childFragmentManager, "dateBottomSheet")
        }
    }

}