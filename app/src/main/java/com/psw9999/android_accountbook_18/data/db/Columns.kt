package com.psw9999.android_accountbook_18.data.db

enum class HistoryColumns {
    id, amount, time, content, payment_id, category_id
}

enum class PaymentColumns {
    id, method
}

enum class CategoryColumns {
    id, isSpend, title, color
}