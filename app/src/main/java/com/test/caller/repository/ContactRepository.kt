package com.test.caller.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.caller.model.ContactInfo
import com.test.caller.utils.SharedPreferencesHelper

class ContactRepository(context: Context) {

    private val sharedPreferencesHelper = SharedPreferencesHelper(context)

    private val _allContacts = MutableLiveData<List<ContactInfo>>()
    val allContacts: LiveData<List<ContactInfo>> get() = _allContacts

    init {
        _allContacts.value = sharedPreferencesHelper.getAllContacts().distinctBy { it.number }
    }

    fun getContactByNumber(number: String): ContactInfo? {
        return sharedPreferencesHelper.getContactByNumber(number)
    }

    fun insertContact(contactInfo: ContactInfo) {
        sharedPreferencesHelper.addContact(contactInfo)
        _allContacts.value = sharedPreferencesHelper.getAllContacts().distinctBy { it.number }
    }

    fun updateBlockedStatus(number: String, isBlocked: Boolean) {
        sharedPreferencesHelper.updateBlockedStatus(number, isBlocked)
        _allContacts.value = sharedPreferencesHelper.getAllContacts().distinctBy { it.number }
    }

    fun getBlockedContacts() {
        _allContacts.value = sharedPreferencesHelper.getAllContacts().distinctBy { it.number }
    }
}
