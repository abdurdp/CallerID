package com.test.caller.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.test.caller.model.ContactInfo

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("contact_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_CONTACT_LIST = "contact_list"
    }

    fun getAllContacts(): List<ContactInfo> {
        val json = sharedPreferences.getString(KEY_CONTACT_LIST, "[]")
        val type = object : TypeToken<List<ContactInfo>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveAllContacts(contacts: List<ContactInfo>) {
        val json = gson.toJson(contacts)
        sharedPreferences.edit().putString(KEY_CONTACT_LIST, json).apply()
    }

    fun addContact(contactInfo: ContactInfo) {
        val contacts = getAllContacts().toMutableList()
        contacts.add(contactInfo)
        saveAllContacts(contacts)
    }

    fun updateBlockedStatus(number: String, isBlocked: Boolean) {
        val contacts = getAllContacts().map { contact ->
            if (contact.number == number) contact.copy(isBlocked = isBlocked) else contact
        }
        saveAllContacts(contacts)
    }

    fun getContactByNumber(number: String): ContactInfo? {
        return getAllContacts().find {
            it.number.replace("[^0-9]".toRegex(), "") == number.replace("[^0-9]".toRegex(), "")
        }
    }
}
