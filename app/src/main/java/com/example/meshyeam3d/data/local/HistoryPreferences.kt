package com.example.meshyeam3d.data.local

import android.content.Context
import com.example.meshyeam3d.data.model.HistoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("meshy_history", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val historyType = object : TypeToken<List<HistoryItem>>() {}.type

    fun getHistory(): List<HistoryItem> {
        val raw = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return runCatching { gson.fromJson<List<HistoryItem>>(raw, historyType) }
            .getOrDefault(emptyList())
    }

    fun addHistory(item: HistoryItem) {
        val next = (listOf(item) + getHistory()).distinctBy { it.id }
        prefs.edit().putString(KEY_HISTORY, gson.toJson(next)).apply()
    }

    companion object {
        private const val KEY_HISTORY = "task_history"
    }
}
