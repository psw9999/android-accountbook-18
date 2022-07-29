package com.psw9999.android_accountbook_18.di

import com.psw9999.android_accountbook_18.data.repository.history.HistoryRepository
import com.psw9999.android_accountbook_18.data.repository.history.HistoryRepositoryImpl
import com.psw9999.android_accountbook_18.data.repository.category.CategoryRepository
import com.psw9999.android_accountbook_18.data.repository.category.CategoryRepositoryImpl
import com.psw9999.android_accountbook_18.data.repository.payment.PaymentRepository
import com.psw9999.android_accountbook_18.data.repository.payment.PaymentRepositoryImpl
import com.psw9999.android_accountbook_18.data.source.local.category.CategoryLocalDataSource
import com.psw9999.android_accountbook_18.data.source.local.history.HistoryLocalDataSource
import com.psw9999.android_accountbook_18.data.source.local.payment.PaymentLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideHistoryRepository(
        localDataSource: HistoryLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ) : HistoryRepository {
        return HistoryRepositoryImpl(localDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providePaymentRepository(
        localDataSource: PaymentLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ) : PaymentRepository {
        return PaymentRepositoryImpl(localDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(
        localDataSource: CategoryLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ) : CategoryRepository {
        return CategoryRepositoryImpl(localDataSource, ioDispatcher)
    }
}