package com.d103.asaf.ui.schedule

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d103.asaf.common.model.dto.AttendanceInfo
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.common.util.AdapterUtil
import com.d103.asaf.databinding.ItemDayMemoBinding
import com.d103.asaf.databinding.ItemStudentAttendanceBinding


class NotiInfoAdapter (context : Context) : ListAdapter<Noti, NotiInfoAdapter.ItemViewHolder>(AdapterUtil.diffUtilNotiInfo) {

    inner class ItemViewHolder(var binding : ItemDayMemoBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(data : Noti){
            binding.notiDate.text = "${data.date.year}년 ${data.date.month} 월 ${data.date.day} 일"
            if(data.notification){
                binding.notiIcon.setColorFilter(Color.YELLOW)
            }
            else{
                binding.notiIcon.setColorFilter(Color.GRAY)
            }
            binding.title.text = data.title

            binding.cardView.setOnClickListener {


            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemDayMemoBinding.inflate(
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