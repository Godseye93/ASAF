package com.d103.asaf.ui.home.pro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d103.asaf.common.model.dto.AttendanceInfo
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.databinding.ItemStudentAttendanceBinding

class UserInfoAdapter(var context : Context) : ListAdapter<AttendanceInfo, UserInfoAdapter.ItemViewHolder>(diffUtil) {


    inner class ItemViewHolder(var binding : ItemStudentAttendanceBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(attendence : AttendanceInfo){
            var user = attendence.member
            binding.userName.text = user.memberName
            Glide.with(context).load(user.profileImage).into(binding.userProfileImage)
            binding.checkBox.setOnClickListener {
                if (it is CheckBox) {
                    val isChecked = it.isChecked // CheckBox의 선택 여부를 확인함
                    itemClickListener.onClick(it, layoutPosition, user, isChecked)
                }

            }
        }
    }

    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtil = object : DiffUtil.ItemCallback<AttendanceInfo>() {
            override fun areItemsTheSame(oldItem: AttendanceInfo, newItem: AttendanceInfo): Boolean {
                return oldItem.attendanceId == newItem.attendanceId
            }

            override fun areContentsTheSame(oldItem: AttendanceInfo, newItem: AttendanceInfo): Boolean {
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
    lateinit var itemClickListener : ItemClickListener
    interface ItemClickListener{
        fun onClick(view: View, position: Int, data: Member, checked : Boolean)
    }


}

class ItemDiffCallback : DiffUtil.ItemCallback<AttendanceInfo>() {
    override fun areItemsTheSame(oldItem: AttendanceInfo, newItem: AttendanceInfo): Boolean {
        return oldItem.attendanceId == newItem.attendanceId
    }

    override fun areContentsTheSame(oldItem: AttendanceInfo, newItem: AttendanceInfo): Boolean {
        return oldItem == newItem
    }
}