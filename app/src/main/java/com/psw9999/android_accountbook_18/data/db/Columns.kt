package com.psw9999.android_accountbook_18.data.db

enum class HistoryColumns(val columnName : String) {
    id("id"),
    amount("amount"),
    time("time"),
    content("content"),
    payment_id("payment_id"),
    category_id("category_id")
}

enum class PaymentColumns(val columnName : String) {
    id("id"),
    method("method")
}

enum class CategoryColumns(val columnName : String) {
    id("id"),
    is_spend("is_spend"),
    title("title"),
    color("color")
}