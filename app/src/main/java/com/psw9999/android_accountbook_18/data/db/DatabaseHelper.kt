package com.psw9999.android_accountbook_18.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "account_book"
        const val DATABASE_VERSION = 1
        const val HISTORY_TABLE = "history_table"
        const val CATEGORY_TABLE = "category_table"
        const val PAYMENT_TABLE = "payment_table"

        fun create(context : Context) : DatabaseHelper {
            return DatabaseHelper(context)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createPaymentQuery = "CREATE TABLE $PAYMENT_TABLE (" +
                "${PaymentColumns.id} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${PaymentColumns.method} TEXT NOT NULL" + ")"

        val createCategoryQuery = "CREATE TABLE $CATEGORY_TABLE (" +
                "${CategoryColumns.id} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${CategoryColumns.isSpend} TEXT NOT NULL, " +
                "${CategoryColumns.title} TEXT NOT NULL, " +
                "${CategoryColumns.color} INTEGER" + ")"

        val createHistoryQuery = "CREATE TABLE $HISTORY_TABLE (" +
                "${HistoryColumns.id} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${HistoryColumns.amount} INTEGER NOT NULL, " +
                "${HistoryColumns.time} TEXT NOT NULL, " +
                "${HistoryColumns.content} TEXT, " +
                "${HistoryColumns.category_id} INTEGER, " +
                "${HistoryColumns.payment_id} INTEGER, " +
                "FOREIGN KEY(${HistoryColumns.payment_id}) REFERENCES $PAYMENT_TABLE(${PaymentColumns.id})," +
                "FOREIGN KEY(${HistoryColumns.category_id}) REFERENCES $CATEGORY_TABLE(${CategoryColumns.id})" + ")"

        db?.let {
            it.execSQL(createPaymentQuery)
            it.execSQL(createCategoryQuery)
            it.execSQL(createHistoryQuery)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVer: Int, newVer: Int) {
        if (oldVer < newVer) {
            db?.execSQL("DROP TABLE IF EXISTS $HISTORY_TABLE")
            onCreate(db)
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        db?.setForeignKeyConstraintsEnabled(true)
    }
}