package com.test.caller.utils

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CustomItemAnimator : DefaultItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        holder?.itemView?.apply {
            alpha = 0f
            animate().alpha(1f).setDuration(500).start()
        }
        return super.animateAdd(holder)
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        holder?.itemView?.apply {
            animate().translationX(width.toFloat()).setDuration(500).withEndAction {
                translationX = 0f // Reset after animation
            }.start()
        }
        return super.animateRemove(holder)
    }
}
