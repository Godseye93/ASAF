package com.d103.asaf.ui.op.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.common.model.dto.DocLocker
import com.d103.asaf.databinding.ItemLockerBinding

// int 형을 Locker DTO로 변경하기
class LockerAdapter : androidx.recyclerview.widget.ListAdapter<DocLocker, LockerAdapter.LockerViewHolder>(LockerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerViewHolder {
        val binding = ItemLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LockerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerViewHolder, position: Int) {
        val locker = getItem(position)
        holder.bind(locker)
    }

    inner class LockerViewHolder(private val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(locker: DocLocker) {
            binding.itemLockerImageviewText.text = locker.name
//            binding.itemLockerImageviewText.text = locker.lockerNum.toString()
        }
    }

    class LockerDiffCallback : DiffUtil.ItemCallback<DocLocker>() {
        override fun areItemsTheSame(oldItem: DocLocker, newItem: DocLocker): Boolean {
            // 식별자 요소를 비교하는게 맞다
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DocLocker, newItem: DocLocker): Boolean {
            return oldItem == newItem
        }
    }
}
