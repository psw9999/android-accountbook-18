package com.psw9999.android_accountbook_18.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.ActivityMainBinding
import com.psw9999.android_accountbook_18.ui.calendar.CalendarFragment
import com.psw9999.android_accountbook_18.ui.configuration.ConfigurationFragment
import com.psw9999.android_accountbook_18.ui.history.HistoryFragment
import com.psw9999.android_accountbook_18.ui.statistics.StatisticsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels()
    private val historyDataViewModel : HistoryDataViewModel by viewModels()
    private val paymentViewModel : PaymentViewModel by viewModels()
    private val categoryViewModel : CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observe()
    }

    override fun onStart() {
        super.onStart()
        historyDataViewModel.getMonthHistorys(
            historyDataViewModel.selectedDate.value.year,
            historyDataViewModel.selectedDate.value.monthValue)
        paymentViewModel.getAllPayments()
        categoryViewModel.getAllCategory()
    }

    private fun observe() {
        binding.lifecycleOwner = this
        binding.mainViewModel = this.mainViewModel

        mainViewModel.currentMenu.observe(this) {
            changeFragment(it)
        }

        // payment, category 수정시 해당 사항 반영되도록 combine 추가
        this.lifecycleScope.launch {
            this@MainActivity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                paymentViewModel.payments.combine(categoryViewModel.category) { payments, category ->
                }.collectLatest {
                    historyDataViewModel.getMonthHistorys(
                        historyDataViewModel.selectedDate.value.year,
                        historyDataViewModel.selectedDate.value.monthValue)
                }
            }
        }
    }

    private fun getFragment(menuType: MainBottomMenuType): Fragment =
        when (menuType) {
            MainBottomMenuType.HISTORY -> HistoryFragment()
            MainBottomMenuType.CALENDAR -> CalendarFragment()
            MainBottomMenuType.STATISTICS -> StatisticsFragment()
            MainBottomMenuType.CONFIGURATION -> ConfigurationFragment()
        }

    private fun changeFragment(menuType: MainBottomMenuType) {
        val transaction = supportFragmentManager.beginTransaction()
        var targetFragment = supportFragmentManager.findFragmentByTag(menuType.tag)
        if (targetFragment == null) {
            targetFragment = getFragment(menuType)
            transaction.add(R.id.l_main_container, targetFragment, menuType.tag)
        }
        transaction.show(targetFragment)
        MainBottomMenuType.values()
            .filterNot { it == menuType }
            .forEach { type ->
                supportFragmentManager.findFragmentByTag(type.tag)?.let {
                    transaction.hide(it)
                }
            }
        transaction.commitAllowingStateLoss()
    }

}