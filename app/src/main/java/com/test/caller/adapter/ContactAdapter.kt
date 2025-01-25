package com.test.caller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.caller.R
import com.test.caller.model.ContactInfo

class ContactAdapter(private val onBlockToggle: (String, Boolean, Int) -> Unit) :
    ListAdapter<ContactInfo, ContactAdapter.ContactViewHolder>(DIFF_CALLBACK) {

    private var fullList: List<ContactInfo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact, onBlockToggle)
    }


    fun setContactList(contactList: List<ContactInfo>) {
        fullList = contactList
        submitList(fullList)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter { contact ->
                contact.name.contains(query, ignoreCase = true) ||
                        contact.number.contains(query)
            }
        }
        submitList(filteredList)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        private val blockButton: Button = itemView.findViewById(R.id.blockButton)

        fun bind(contact: ContactInfo, onBlockToggle: (String, Boolean, Int) -> Unit) {
            nameTextView.text = contact.name
            numberTextView.text = contact.number
            blockButton.text = if (contact.isBlocked) "Unblock" else "Block"
            blockButton.setOnClickListener {
                onBlockToggle(contact.number, contact.isBlocked,bindingAdapterPosition)

            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContactInfo>() {
            override fun areItemsTheSame(oldItem: ContactInfo, newItem: ContactInfo): Boolean {
                return oldItem.number == newItem.number
            }

            override fun areContentsTheSame(oldItem: ContactInfo, newItem: ContactInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}
