package com.psw9999.android_accountbook_18.di

import android.content.Context
import com.psw9999.android_accountbook_18.data.db.DatabaseHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context : Context) : DatabaseHelper {
        return DatabaseHelper.create(context)
    }

}