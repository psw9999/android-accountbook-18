package com.psw9999.android_accountbook_18.ui.configuration

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.databinding.FragmentConfigurationBinding
import com.psw9999.android_accountbook_18.ui.categoryadd.CategoryAddFragment
import com.psw9999.android_accountbook_18.ui.categoryadd.CategoryAddFragment.Companion.CATEGORY_ITEM
import com.psw9999.android_accountbook_18.ui.common.BaseFragment
import com.psw9999.android_accountbook_18.ui.main.CategoryViewModel
import com.psw9999.android_accountbook_18.ui.main.PaymentViewModel
import com.psw9999.android_accountbook_18.ui.paymentadd.PaymentAddFragment
import com.psw9999.android_accountbook_18.ui.paymentadd.PaymentAddFragment.Companion.PAYMENT_ITEM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigurationFragment :
    BaseFragment<FragmentConfigurationBinding>(R.layout.fragment_configuration) {

    private val paymentViewModel: PaymentViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()

    private val paymentEditClickListener : ((PaymentDto) -> Unit) = { paymentDto ->
        val transaction = activity!!.supportFragmentManager.beginTransaction()
            .add(R.id.l_main_container,
            PaymentAddFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable(PAYMENT_ITEM, paymentDto)
                }
            })
        transaction.addToBackStack(null)
        transaction.hide(this)
        transaction.commit()
    }

    private val categoryEditClickListener : ((CategoryDto) -> Unit) = { categoryDto ->
        val transaction = activity!!.supportFragmentManager.beginTransaction()
            .add(R.id.l_main_container,
                CategoryAddFragment().apply {
                    arguments = Bundle().apply {
                        this.putParcelable(CATEGORY_ITEM, categoryDto)
                    }
                })
        transaction.addToBackStack(null)
        transaction.hide(this)
        transaction.commit()
    }

    private val paymentAddClickListener : (() -> Unit) = {
        val transaction = activity!!.supportFragmentManager.beginTransaction().add(R.id.l_main_container, PaymentAddFragment())
        transaction.addToBackStack(null)
        transaction.hide(this)
        transaction.commit()
    }

    private val categoryAddClickListener : ((Boolean) -> Unit) = { isSpend->
        val transaction = activity!!.supportFragmentManager.beginTransaction()
            .add(R.id.l_main_container, CategoryAddFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(CategoryAddFragment.IS_SPEND, isSpend)
                }
            })
        transaction.addToBackStack(null)
        transaction.hide(this)
        transaction.commit()
    }

    override fun initViews() {
        binding.cvConfiguration.setContent {
            ConfigurationScreen(
                paymentViewModel,
                categoryViewModel,
                paymentEditClickListener,
                categoryEditClickListener,
                paymentAddClickListener,
                categoryAddClickListener
            )
        }
    }

    override fun observe() {

    }

    override fun onStart() {
        super.onStart()
        paymentViewModel.getAllPayments()
        categoryViewModel.getAllCategory()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            paymentViewModel.getAllPayments()
            categoryViewModel.getAllCategory()
        }
    }
}