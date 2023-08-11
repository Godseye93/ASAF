package com.d103.asaf.ui.market

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.AdapterUtil
import com.d103.asaf.databinding.ItemMarketBinding
import com.d103.asaf.databinding.ItemStudentAttendanceBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MarketAdpater (var context : Context): ListAdapter<Market, MarketAdpater.ItemViewHolder>(
    AdapterUtil.diffUtilMarket) {

    inner class ItemViewHolder(var binding : ItemMarketBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(data : Market){
            val imageSplit = data.profileImage.split("/")
            val path =  "http://i9d103.p.ssafy.io" + "/" + imageSplit[4] + "/" + imageSplit[5] + "/" + imageSplit[6]
            Log.d("이미지 경로", "${ApplicationClass.API_URL}member/${data.profileImage.split("/")[6].split(".")[0]}.com/profile-image")
            Glide
                .with(context)
                .load(path)
                .into( binding.marketProfileImage)
            binding.markentPostTitle.text = data.title
            binding.markentPostTitle.isSelected = true
            val dateFormat = SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault())
            val registerDate = dateFormat.format(Date(data.registerTime))
            binding.marketPostRegisterDate.text = registerDate

            binding.itemMarketLayout.setOnClickListener {

                itemClickListener.onClick(it, layoutPosition, data)


            }


        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarketAdpater.ItemViewHolder {
        return ItemViewHolder(
            ItemMarketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MarketAdpater.ItemViewHolder, position: Int) {
        holder.bind(currentList[position])

    }

    lateinit var itemClickListener : ItemClickListener
    interface ItemClickListener{
        fun onClick(view: View, position: Int, data: Market)
    }


}