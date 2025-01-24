package com.test.caller.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.caller.model.CallerInfo
import com.test.caller.utils.SharedPreferencesHelper

class CallerRepository(context: Context) {

    private val sharedPreferencesHelper = SharedPreferencesHelper(context)

    private val _allCallers = MutableLiveData<List<CallerInfo>>()
    val allCallers: LiveData<List<CallerInfo>> get() = _allCallers

    init {
        _allCallers.value = sharedPreferencesHelper.getAllCallers().distinctBy { it.number }
    }

    fun getCallerByNumber(number: String): CallerInfo? {
        return sharedPreferencesHelper.getCallerByNumber(number)
    }

    fun insertCaller(callerInfo: CallerInfo) {
        sharedPreferencesHelper.addCaller(callerInfo)
        _allCallers.value = sharedPreferencesHelper.getAllCallers().distinctBy { it.number }
    }

    fun updateBlockedStatus(number: String, isBlocked: Boolean) {
        sharedPreferencesHelper.updateBlockedStatus(number, isBlocked)
        _allCallers.value = sharedPreferencesHelper.getAllCallers().distinctBy { it.number }
    }

    fun getBlockedCallers() {
        _allCallers.value = sharedPreferencesHelper.getAllCallers().distinctBy { it.number }
    }
}
