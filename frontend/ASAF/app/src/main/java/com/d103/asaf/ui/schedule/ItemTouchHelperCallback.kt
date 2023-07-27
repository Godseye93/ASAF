package com.d103.asaf.ui.schedule

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.ui.schedule.NotiInfoAdapter

class ItemTouchHelperCallback(private val adapter: NotiInfoAdapter) : ItemTouchHelper.Callback() {


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = 0
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val position = viewHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            // notiList에서 해당 아이템 삭제
            adapter.removeItem(position)
        }
    }
}