package com.test.caller

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.caller.adapter.CallerAdapter
import com.test.caller.databinding.ActivityMainBinding
import com.test.caller.model.CallerInfo
import com.test.caller.utils.CustomItemAnimator
import com.test.caller.viewmodel.CallerViewModel
import com.test.caller.views.BlockedContactsActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CallerViewModel
    private lateinit var adapter: CallerAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CallerViewModel::class.java]


        setSupportActionBar(binding.toolbar)
        adapter = CallerAdapter { number, isBlocked,position ->
            viewModel.updateBlockedStatus(number, !isBlocked)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = CustomItemAnimator()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.allCallers.observe(this) { callers ->
            adapter.setCallerList(callers.filter { !it.isBlocked }.sortedBy { it.name.lowercase() })
        }

        requestPermissions()

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            if (adapter.currentList.isEmpty()) {
                fetchContacts()
            }
        }
    }

    private fun requestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.POST_NOTIFICATIONS,
            )
        } else {
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
            )
        }
        ActivityCompat.requestPermissions(this, permissions, 1001)
    }

    @SuppressLint("Range")
    private fun fetchContacts() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val contentResolver = contentResolver
            val pageSize = 0
            val offset = 20
            val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIMIT $pageSize OFFSET $offset"
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val name =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    Log.d(
                        "Contact",
                        "name->" + name + " number-> " + number.replace("[^0-9]".toRegex(), "")
                    )
                    viewModel.insertCaller(CallerInfo(number, name, false))
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            fetchContacts()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.WHITE)
        searchEditText.setHintTextColor(Color.LTGRAY)
        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.WHITE)
        val closeIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeIcon.setColorFilter(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText.orEmpty())
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_blocked -> {
                val intent = Intent(this, BlockedContactsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}