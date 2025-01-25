package com.test.caller.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.caller.adapter.ContactAdapter
import com.test.caller.databinding.ActivityBlockedContactsBinding
import com.test.caller.viewmodel.ContactViewModel

class BlockedContactsActivity : AppCompatActivity() {
    private lateinit var adapter: ContactAdapter
    private lateinit var binding: ActivityBlockedContactsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlockedContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ContactViewModel(this.application)
        adapter = ContactAdapter { number, isBlocked, position ->
            viewModel.updateBlockedStatus(number, !isBlocked)
        }
        binding.recyclerViewBlocked.adapter = adapter
        binding.recyclerViewBlocked.layoutManager = LinearLayoutManager(this)

        viewModel.getBlockedContacts()
        viewModel.allContacts.observe(this) { blockedContacts ->
            adapter.setContactList(blockedContacts.filter { it.isBlocked }
                .sortedBy { it.name.lowercase() })
            if (adapter.currentList.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
        }
    }

}