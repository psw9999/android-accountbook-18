package com.psw9999.android_accountbook_18.di

import com.psw9999.android_accountbook_18.data.source.local.history.HistoryDataSource
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import com.psw9999.android_accountbook_18.data.source.local.category.CategoryDataSource
import com.psw9999.android_accountbook_18.data.source.local.category.CategoryLocalDataSource
import com.psw9999.android_accountbook_18.data.source.local.history.HistoryLocalDataSource
import com.psw9999.android_accountbook_18.data.source.local.payment.PaymentDataSource
import com.psw9999.android_accountbook_18.data.source.local.payment.PaymentLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideHistoryLocalDataSource(
        dataBaseHelper: DatabaseHelper,
        ioDispatcher: CoroutineDispatcher
    ) : HistoryDataSource {
        return HistoryLocalDataSource(dataBaseHelper, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providePaymentLocalDataSource(
        dataBaseHelper: DatabaseHelper,
        ioDispatcher: CoroutineDispatcher
    ) : PaymentDataSource {
        return PaymentLocalDataSource(dataBaseHelper, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideCategoryLocalDataSource(
        dataBaseHelper : DatabaseHelper,
        ioDispatcher: CoroutineDispatcher
    ) : CategoryDataSource {
        return CategoryLocalDataSource(dataBaseHelper, ioDispatcher)
    }

}