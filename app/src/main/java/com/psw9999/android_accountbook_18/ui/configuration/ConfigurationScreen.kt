package com.psw9999.android_accountbook_18.ui.configuration

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.psw9999.android_accountbook_18.R
import com.psw9999.android_accountbook_18.data.dto.CategoryDto
import com.psw9999.android_accountbook_18.data.dto.PaymentDto
import com.psw9999.android_accountbook_18.ui.configuration.item.*
import com.psw9999.android_accountbook_18.ui.main.CategoryViewModel
import com.psw9999.android_accountbook_18.ui.main.PaymentViewModel

@Composable
fun ConfigurationScreen(
    paymentViewModel: PaymentViewModel,
    categoryViewModel: CategoryViewModel,
    paymentEditClickListener : ((PaymentDto) -> Unit),
    categoryEditClickListener : ((CategoryDto) -> Unit),
    paymentAddClickListener : (() -> Unit),
    categoryAddClickListener : ((Boolean) -> Unit)
) {
    val paymentList by paymentViewModel.payments.collectAsState()
    val categoryList by categoryViewModel.category.collectAsState()

    Scaffold(
        topBar = { ConfigurationAppbar(title = "설정") },
        backgroundColor = colorResource(R.color.off_white)
    ) {
        LazyColumn(Modifier.padding(it)) {
            ConfigurationType.values().forEach { configurationType ->
                item {
                    ConfigurationHeader(title = configurationType.title)
                }

                when (configurationType) {
                    ConfigurationType.Payment ->
                        items(items = paymentList) { payment ->
                            ConfigurationPayment(paymentName = payment.method) {
                                paymentEditClickListener.invoke(payment)
                            }
                        }
                    ConfigurationType.SpendCategory ->
                        items(items = categoryList.filter { it.isSpend }) { category ->
                            ConfigurationCategory(category = category) {
                                categoryEditClickListener.invoke(category)
                            }
                        }
                    ConfigurationType.IncomeCategory ->
                        items(items = categoryList.filter { !it.isSpend }) { category ->
                            ConfigurationCategory(category = category) {
                                categoryEditClickListener.invoke(category)
                            }
                        }
                }

                item {
                    ConfigurationAddContent(type = configurationType.title) {
                        when(configurationType) {
                            ConfigurationType.Payment -> paymentAddClickListener()
                            ConfigurationType.SpendCategory -> categoryAddClickListener(true)
                            ConfigurationType.IncomeCategory -> categoryAddClickListener(false)
                        }
                    }
                }
            }
        }
    }
}

enum class ConfigurationType(val title: String) {
    Payment("결제수단"),
    SpendCategory("지출 카테고리"),
    IncomeCategory("수입 카테고리")
}