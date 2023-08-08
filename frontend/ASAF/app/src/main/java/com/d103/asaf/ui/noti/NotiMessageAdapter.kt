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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotiMessageAdapter (var context : Context) : ListAdapter<NotiMessage, NotiMessageAdapter.ItemViewHolder>(
    AdapterUtil.diffUtilNotiMessage
) {
    var hiddenFlag = false
    inner class ItemViewHolder(var binding : NotiCardViewBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(data : NotiMessage){
//            Log.d("이미지", "${"${ApplicationClass.API_URL}member/${data.senderImage.split("/")[6].split(".")[0]}.com/profile-image"} ")
            Glide
                .with(context)
                .load( "${API_URL}member/ssafypro0001@ssafy.com/profile-image")
                .into( binding.senderProfileImage)
            binding.notiTitle.text = data.title
            binding.senderName.text = data.sender
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.getDefault())
            val registerDate = dateFormat.format(Date(data.sendTime))
            binding.notiCardRegisterTime.text = registerDate
            binding.notiCardContent.text = data.content

            binding.fragmentStudentNotiLayout.setOnClickListener {
                hiddenFlag = !hiddenFlag

                if(hiddenFlag){
                    binding.hiddenLayout.visibility = View.VISIBLE
                }
                else{
                    binding.hiddenLayout.visibility = View.GONE
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