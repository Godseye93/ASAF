package com.d103.asaf.ui.op.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.databinding.ItemLockerBinding

// int 형을 Locker DTO로 변경하기
class LockerAdapter : androidx.recyclerview.widget.ListAdapter<Int, LockerAdapter.LockerViewHolder>(LockerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerViewHolder {
        val binding = ItemLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LockerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

    inner class LockerViewHolder(private val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(Locker: Int) {
            binding.itemLockerImageviewText.text = Locker.toString()
        }
    }

    class LockerDiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            // 식별자 요소를 비교하는게 맞다
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
}
