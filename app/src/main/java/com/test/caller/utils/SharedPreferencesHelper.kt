package com.test.caller.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.test.caller.model.CallerInfo

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("caller_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_CALLER_LIST = "caller_list"
    }

    fun getAllCallers(): List<CallerInfo> {
        val json = sharedPreferences.getString(KEY_CALLER_LIST, "[]") // Default to an empty list
        val type = object : TypeToken<List<CallerInfo>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveAllCallers(callers: List<CallerInfo>) {
        val json = gson.toJson(callers)
        sharedPreferences.edit().putString(KEY_CALLER_LIST, json).apply()
    }

    fun addCaller(callerInfo: CallerInfo) {
        val callers = getAllCallers().toMutableList()
        callers.add(callerInfo)
        saveAllCallers(callers)
    }

    fun updateBlockedStatus(number: String, isBlocked: Boolean) {
        val callers = getAllCallers().map { caller ->
            if (caller.number == number) caller.copy(isBlocked = isBlocked) else caller
        }
        saveAllCallers(callers)
    }

    fun getCallerByNumber(number: String): CallerInfo? {
        return getAllCallers().find {
            it.number.replace("[^0-9]".toRegex(), "") == number.replace("[^0-9]".toRegex(), "")
        }
    }
}
