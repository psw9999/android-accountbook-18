package com.psw9999.android_accountbook_18

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AccountBookApplication : Application() {

    companion object {
        private lateinit var instance: Application

        fun applicationContext(): Context {
            return instance
        }
    }

    init {
        instance = this
    }

}