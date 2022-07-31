package com.psw9999.android_accountbook_18.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "account_book_db"
        const val DATABASE_VERSION = 1
        const val HISTORY_TABLE = "history_table"
        const val CATEGORY_TABLE = "category_table"
        const val PAYMENT_TABLE = "payment_table"

        fun create(context: Context): DatabaseHelper {
            return DatabaseHelper(context)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createPaymentQuery = "CREATE TABLE $PAYMENT_TABLE (" +
                "${PaymentColumns.id.columnName} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${PaymentColumns.method.columnName} TEXT NOT NULL" + ")"

        val createCategoryQuery = "CREATE TABLE $CATEGORY_TABLE (" +
                "${CategoryColumns.id.columnName} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${CategoryColumns.is_spend.columnName} INT NOT NULL, " +
                "${CategoryColumns.title.columnName} TEXT NOT NULL, " +
                "${CategoryColumns.color.columnName} INTEGER NOT NULL" + ")"

        val createHistoryQuery = "CREATE TABLE $HISTORY_TABLE (" +
                "${HistoryColumns.id.columnName} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${HistoryColumns.amount.columnName} INTEGER NOT NULL, " +
                "${HistoryColumns.time.columnName} DATE NOT NULL, " +
                "${HistoryColumns.content.columnName} TEXT, " +
                "${HistoryColumns.category_id.columnName} INTEGER, " +
                "${HistoryColumns.payment_id.columnName} INTEGER, " +
                "FOREIGN KEY(${HistoryColumns.payment_id.columnName}) REFERENCES $PAYMENT_TABLE(${PaymentColumns.id.columnName})," +
                "FOREIGN KEY(${HistoryColumns.category_id.columnName}) REFERENCES $CATEGORY_TABLE(${CategoryColumns.id.columnName})" + ")"

        db?.let {
            it.execSQL(createPaymentQuery)
            it.execSQL(createCategoryQuery)
            it.execSQL(createHistoryQuery)
            createDefaultValue(db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVer: Int, newVer: Int) {
        if (oldVer < newVer) {
            db?.execSQL("DROP TABLE IF EXISTS $HISTORY_TABLE")
            db?.execSQL("DROP TABLE IF EXISTS $CATEGORY_TABLE")
            db?.execSQL("DROP TABLE IF EXISTS $PAYMENT_TABLE")
            onCreate(db)
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        db?.setForeignKeyConstraintsEnabled(true)
    }

    private fun createDefaultValue(db: SQLiteDatabase) {
        val paymentDefault = ContentValues().apply {
            put(PaymentColumns.id.columnName, 1)
            put(PaymentColumns.method.columnName, "현금")
        }
        val incomeCategoryDefault = ContentValues().apply {
            put(CategoryColumns.id.columnName, 1)
            put(CategoryColumns.is_spend.columnName, 0)
            put(CategoryColumns.title.columnName, "미분류")
            put(CategoryColumns.color.columnName, -6565502)
        }
        val spendCategoryDefault = ContentValues().apply {
            put(CategoryColumns.id.columnName, 2)
            put(CategoryColumns.is_spend.columnName, 1)
            put(CategoryColumns.title.columnName, "미분류")
            put(CategoryColumns.color.columnName, -11899709)
        }
        db.insert(PAYMENT_TABLE, null, paymentDefault)
        db.insert(CATEGORY_TABLE, null, incomeCategoryDefault)
        db.insert(CATEGORY_TABLE, null, spendCategoryDefault)
    }
}