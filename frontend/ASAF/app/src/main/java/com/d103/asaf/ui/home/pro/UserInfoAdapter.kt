package com.d103.asaf.ui.home.pro

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d103.asaf.common.model.dto.Attendance
import com.d103.asaf.databinding.ItemStudentAttendanceBinding

class UserInfoAdapter(var context : Context) : ListAdapter<Attendance, UserInfoAdapter.ItemViewHolder>(diffUtil) {


    inner class ItemViewHolder(var binding : ItemStudentAttendanceBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(attendence : Attendance){
            var user = attendence.user
            binding.userName.text = user.name
            Glide.with(context).load(user.profileImage).into(binding.userProfileImage)
        }
    }

    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtil = object : DiffUtil.ItemCallback<Attendance>() {
            override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemStudentAttendanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}
class ItemDiffCallback : DiffUtil.ItemCallback<Attendance>() {
    override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
        return oldItem == newItem
    }
}