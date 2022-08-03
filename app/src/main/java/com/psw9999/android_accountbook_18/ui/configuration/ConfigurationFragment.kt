package com.psw9999.android_accountbook_18.ui.configuration

import androidx.fragment.app.activityViewModels
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.databinding.FragmentConfigurationBinding
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.main.CategoryViewModel
import com.psw9999.android_accountbook_18.ui.main.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment :
    BaseFragment<FragmentConfigurationBinding>(R.layout.fragment_configuration) {

    private val paymentViewModel: PaymentViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()

    override fun initViews() {
        binding.cvConfiguration.setContent {
            ConfigurationScreen(paymentViewModel, categoryViewModel)
        }
    }

    override fun observe() {

    }
}