package com.psw9999.android_accountbook_18.util

import android.widget.Toast
import com.psw9999.android_accountbook_18.AccountBookApplication.Companion.applicationContext

fun toast(message : String) {
    Toast.makeText(applicationContext(), message, Toast.LENGTH_SHORT).show()
}