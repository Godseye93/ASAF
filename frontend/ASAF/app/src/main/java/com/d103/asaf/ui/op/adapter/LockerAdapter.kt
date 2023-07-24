package com.d103.asaf.ui.op.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.databinding.ItemLockerBinding

class LockerAdapter(private val lockerList: MutableList<String>) : RecyclerView.Adapter<LockerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            // 아이템의 내용을 설정합니다.
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lockerList[position])
    }


    override fun getItemCount(): Int {
        return 80
    }
}