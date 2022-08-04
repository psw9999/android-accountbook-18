package com.psw9999.android_accountbook_18.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.psw9999.android_accountbook_18.databinding.FragmentDateBottomSheetBinding
import com.psw9999.android_accountbook_18.ui.main.HistoryDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DateBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentDateBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val historyDataViewModel : HistoryDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDateBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheet()
    }

    private fun initBottomSheet() {
        binding.btnBsRegister.setOnClickListener {
            historyDataViewModel.setSelectedDate(binding.bsYearPicker.value, binding.bsMonthPicker.value )
            dismiss()
        }
        binding.bsYearPicker.minValue = 2000
        binding.bsYearPicker.maxValue = 2050
        binding.bsYearPicker.value = historyDataViewModel.selectedDate.value.year
        binding.bsMonthPicker.minValue = 1
        binding.bsMonthPicker.maxValue = 12
        binding.bsMonthPicker.value = historyDataViewModel.selectedDate.value.monthValue
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}