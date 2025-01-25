package com.test.caller.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.test.caller.model.ContactInfo
import com.test.caller.repository.ContactRepository

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ContactRepository(application)
    val allContacts: LiveData<List<ContactInfo>> = repository.allContacts

    fun getContactByNumber(number: String): ContactInfo? {
        return repository.getContactByNumber(number)
    }

    fun insertContact(contactInfo: ContactInfo) {
        repository.insertContact(contactInfo)
    }

    fun updateBlockedStatus(number: String, isBlocked: Boolean) {
        repository.updateBlockedStatus(number, isBlocked)
    }

    fun getBlockedContacts() {
        repository.getBlockedContacts()
    }
}
