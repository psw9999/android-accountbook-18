package com.psw9999.android_accountbook_18.ui.statistics

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.model.HistoryItem
import com.psw9999.android_accountbook_18.data.model.StatisticsItem
import com.psw9999.android_accountbook_18.databinding.FragmentStatisticsBinding
import com.psw9999.android_accountbook_18.ui.bottomsheet.DateBottomSheet
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.main.HistoryDataViewModel
import com.psw9999.android_accountbook_18.ui.statistics.adapter.StatisticsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics){

    private val historyDataViewModel : HistoryDataViewModel by activityViewModels()
    private val statisticsListAdapter : StatisticsListAdapter by lazy { StatisticsListAdapter() }

    override fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                historyDataViewModel.histories.collectLatest {
                    loadHistoryData(it)
                }
            }
        }
    }

    override fun initViews() {
        binding.historyDataViewModel = historyDataViewModel
        binding.rvStatisticsCategory.adapter = statisticsListAdapter
        setupPieChart()
        initBottomSheet()
    }

    private fun setupPieChart() {
        with(binding.statisticsPiechart) {
            isRotationEnabled = false
            setDrawEntryLabels(false)
            legend.isEnabled = false
            description.isEnabled = false
        }
    }

    private fun loadHistoryData(histories : List<HistoryItem>) {
        // 카테고리 별 지출 합계 구하기
        val categorySum = mutableMapOf<Pair<String,Int>, Int>()
        histories.forEach { history ->
            if (history.isSpend) {
                val pair = Pair(history.category, history.color)
                categorySum[pair] = categorySum.getOrDefault(pair, 0) + history.amount
            }
        }
        setPieChartData(categorySum)
        setRecyclerViewData(categorySum)
    }

    private fun setPieChartData(categorySum : MutableMap<Pair<String, Int>, Int>) {
        val pieEntries = ArrayList<PieEntry>()
        val colorLists = ArrayList<Int>()

        categorySum.forEach { (category, amount) ->
            pieEntries.add(PieEntry(amount.toFloat(), category.first))
            colorLists.add(category.second)
        }

        val dataSet = PieDataSet(pieEntries,"")
        dataSet.colors = colorLists

        val data = PieData(dataSet)
        data.setDrawValues(false)

        binding.statisticsPiechart.data = data
        binding.statisticsPiechart.animateY(1000, Easing.EaseInOutQuad)
        binding.statisticsPiechart.invalidate()
    }

    private fun setRecyclerViewData(categorySum : MutableMap<Pair<String, Int>, Int>) {
        val categoryList = categorySum.toList().sortedBy { it.second }
        val statisticsList = arrayListOf<StatisticsItem>().apply {
            categoryList.forEach {
                this.add(
                    StatisticsItem(
                        category = it.first.first,
                        categoryColor = it.first.second,
                        spendAmount = it.second,
                        percent = ((it.second.toDouble() / historyDataViewModel.spendSum.value) * 100).roundToInt()
                    )
                )
            }
        }
        statisticsListAdapter.submitList(statisticsList)
    }

    private fun initBottomSheet() {
        binding.abStatisticsDate.setOnTitleClickListener {
            val bottomSheet = DateBottomSheet()
            bottomSheet.show(childFragmentManager, "dateBottomSheet")
        }
    }

}