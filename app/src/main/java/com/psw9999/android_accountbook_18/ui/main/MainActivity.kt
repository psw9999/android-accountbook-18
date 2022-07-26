package com.psw9999.android_accountbook_18.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.ActivityMainBinding
import com.psw9999.android_accountbook_18.ui.calendar.CalendarFragment
import com.psw9999.android_accountbook_18.ui.configuration.ConfigurationFragment
import com.psw9999.android_accountbook_18.ui.history.HistoryFragment
import com.psw9999.android_accountbook_18.ui.statistics.StatisticsFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // 데이터베이스 접근으로 인해 applicationContext 주입 고려하기
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observe()
    }

    private fun observe() {
        binding.lifecycleOwner = this
        binding.mainViewModel = this.mainViewModel
        mainViewModel.currentMenu.observe(this) {
            changeFragment(it)
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