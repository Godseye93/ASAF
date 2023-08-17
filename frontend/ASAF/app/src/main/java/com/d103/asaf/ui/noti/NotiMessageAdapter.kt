package com.d103.asaf.ui.noti

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.ApplicationClass.Companion.API_URL
import com.d103.asaf.common.model.Room.NotiMessage
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.util.AdapterUtil
import com.d103.asaf.databinding.ItemMarketBinding
import com.d103.asaf.databinding.NotiCardViewBinding
import com.d103.asaf.ui.market.MarketAdpater
import com.d103.asaf.ui.schedule.ScheduleFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotiMessageAdapter (var context : Context,var fragment: StudentNotiFragment) : ListAdapter<NotiMessage, NotiMessageAdapter.ItemViewHolder>(
    AdapterUtil.diffUtilNotiMessage
) {
    // 스와이프한 아이템 삭제
    fun removeItem(position: Int) {
        if (position in 0 until itemCount) {
            val newList = ArrayList(currentList)
            (fragment as StudentNotiFragment).deleteMessage(currentList[position])
            newList.removeAt(position)
            submitList(newList)
        }
    }

    inner class ItemViewHolder(var binding : NotiCardViewBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(data : NotiMessage){
//            Log.d("이미지", "${"${ApplicationClass.API_URL}member/${data.senderImage.split("/")[6].split(".")[0]}.com/profile-image"} ")
          val imageSplit = data.senderImage.split("/")!!
            val path =  "http://i9d103.p.ssafy.io" + "/" + imageSplit[4] + "/" + imageSplit[5] + "/" + imageSplit[6]
            Glide
                .with(context)
                .load( path)
                .into( binding.senderProfileImage)
            binding.notiTitle.text = data.title
            binding.senderName.text = data.sender
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.getDefault())
            val registerDate = dateFormat.format(Date(data.sendTime))
            binding.notiCardRegisterTime.text = registerDate
            binding.notiCardContent.text = data.content

            binding.fragmentStudentNotiLayout.setOnClickListener {

                if(binding.hiddenLayout.visibility == View.VISIBLE){
                    binding.hiddenLayout.visibility = View.GONE
                }
                else{
                    binding.hiddenLayout.visibility = View.VISIBLE
                }



            }


        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotiMessageAdapter.ItemViewHolder {
        return ItemViewHolder(
            NotiCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotiMessageAdapter.ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


}