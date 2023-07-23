package com.d103.asaf.ui.op.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.databinding.ItemSeatBinding

// String -> Seat DTO로 변경하기
class SeatAdapter : ListAdapter<String, SeatAdapter.SeatViewHolder>(SeatDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSeatBinding.inflate(inflater, parent, false)
        return SeatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

    inner class SeatViewHolder(private val binding: ItemSeatBinding) : RecyclerView.ViewHolder(binding.root) {
        // dto 내용을 xml에 합치는 함수
        fun bind(seat: String) {
            binding.apply {

            }
        }
    }

    class SeatDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return true // oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}