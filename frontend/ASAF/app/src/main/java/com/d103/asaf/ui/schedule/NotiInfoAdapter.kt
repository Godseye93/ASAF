package com.d103.asaf.ui.schedule

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.common.util.AdapterUtil
import com.d103.asaf.databinding.ItemDayMemoBinding
import com.d103.asaf.databinding.ItemStudentAttendanceBinding



class NotiInfoAdapter (context : Context) : ListAdapter<Noti, NotiInfoAdapter.ItemViewHolder>(AdapterUtil.diffUtilNotiInfo) {

    private var _context = context

    // 스와이프한 아이템 삭제
    fun removeItem(position: Int) {
        if (position in 0 until itemCount) {
            val newList = ArrayList(currentList)
            newList.removeAt(position)
            submitList(newList)
        }
    }

    inner class ItemViewHolder(var binding : ItemDayMemoBinding) :  RecyclerView.ViewHolder(binding.root) {


        fun bind(data : Noti){
            binding.notiDate.text = "${data.sendTime.year}년 ${data.sendTime.month} 월 ${data.sendTime.day} 일"
            if(data.notification){
                binding.notiIcon.setColorFilter(Color.YELLOW)
            }
            else{
                binding.notiIcon.setColorFilter(Color.GRAY)
            }
            binding.title.text = data.title

            binding.cardView.setOnClickListener {

                val customDialog = NotiCustomDialog(_context, data.content)
                customDialog.show()
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