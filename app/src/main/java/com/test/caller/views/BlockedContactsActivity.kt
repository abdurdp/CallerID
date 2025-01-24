package com.test.caller.views

import android.os.Bundle
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.caller.R
import com.test.caller.adapter.CallerAdapter
import com.test.caller.databinding.ActivityBlockedContactsBinding
import com.test.caller.viewmodel.CallerViewModel

class BlockedContactsActivity : AppCompatActivity() {
    private lateinit var adapter: CallerAdapter
    private lateinit var binding: ActivityBlockedContactsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlockedContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = CallerViewModel(this.application)
        adapter = CallerAdapter { number, isBlocked ,position->
            viewModel.updateBlockedStatus(number, !isBlocked)
        }
        binding.recyclerViewBlocked.adapter = adapter
        binding.recyclerViewBlocked.layoutManager = LinearLayoutManager(this)

        viewModel.getBlockedCallers()
        viewModel.allCallers.observe(this) { blockedContacts ->
            adapter.setCallerList(blockedContacts.filter { it.isBlocked }
                .sortedBy { it.name.lowercase() })
            if (adapter.currentList.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
        }
    }

}