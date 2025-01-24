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
import com.test.caller.model.CallerInfo

class CallerAdapter(private val onBlockToggle: (String, Boolean) -> Unit) :
    ListAdapter<CallerInfo, CallerAdapter.CallerViewHolder>(DIFF_CALLBACK) {

    private var fullList: List<CallerInfo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_caller, parent, false)
        return CallerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallerViewHolder, position: Int) {
        val caller = getItem(position)
        holder.bind(caller, onBlockToggle)
    }


    fun setCallerList(callerList: List<CallerInfo>) {
        fullList = callerList
        submitList(fullList)
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter { caller ->
                caller.name.contains(query, ignoreCase = true) ||
                        caller.number.contains(query)
            }
        }
        submitList(filteredList)
    }

    class CallerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        private val blockButton: Button = itemView.findViewById(R.id.blockButton)

        fun bind(caller: CallerInfo, onBlockToggle: (String, Boolean) -> Unit) {
            nameTextView.text = caller.name
            numberTextView.text = caller.number
            blockButton.text = if (caller.isBlocked) "Unblock" else "Block"
            blockButton.setOnClickListener {
                onBlockToggle(caller.number, caller.isBlocked)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CallerInfo>() {
            override fun areItemsTheSame(oldItem: CallerInfo, newItem: CallerInfo): Boolean {
                return oldItem.number == newItem.number
            }

            override fun areContentsTheSame(oldItem: CallerInfo, newItem: CallerInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}
