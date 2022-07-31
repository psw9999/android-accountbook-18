package com.psw9999.android_accountbook_18.data.model

sealed class HistoryListItem {
    data class HistoryContent(val history : HistoryItem) : HistoryListItem()

    data class HistoryHeader(
        val date : String,
        var spend : Int = 0,
        var income : Int = 0
    ) : HistoryListItem()
}
