package com.test.caller.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.test.caller.model.CallerInfo
import com.test.caller.repository.CallerRepository

class CallerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CallerRepository(application)
    val allCallers: LiveData<List<CallerInfo>> = repository.allCallers

    fun getCallerByNumber(number: String): CallerInfo? {
        return repository.getCallerByNumber(number)
    }

    fun insertCaller(callerInfo: CallerInfo) {
        repository.insertCaller(callerInfo)
    }

    fun updateBlockedStatus(number: String, isBlocked: Boolean) {
        repository.updateBlockedStatus(number, isBlocked)
    }

    fun getBlockedCallers() {
        repository.getBlockedCallers()
    }
}
