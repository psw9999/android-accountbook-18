package com.psw9999.android_accountbook_18.ui.historyinput

import android.app.DatePickerDialog
import android.util.Log
import androidx.fragment.app.viewModels
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.FragmentHistoryInputBinding
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.util.DateUtil.currentDate
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HistoryInputFragment :
    BaseFragment<FragmentHistoryInputBinding>(R.layout.fragment_history_input) {

    private val historyInputViewModel : HistoryInputViewModel by viewModels()

    override fun initViews() {
        binding.viewmodel = historyInputViewModel

        // 화면 config 대응
        val date = historyInputViewModel.historyDate.value.split("-")
        val datePicker = DatePickerDialog(
            activityContext, { _, year, month, dayOfMonth ->
                historyInputViewModel.setHistoryDate(year, month, dayOfMonth) },
            date[0].toInt(), date[1].toInt()-1, date[2].toInt())

        binding.tvDate.setOnClickListener {
            datePicker.show()
        }
    }

    override fun observe() {
    }

}