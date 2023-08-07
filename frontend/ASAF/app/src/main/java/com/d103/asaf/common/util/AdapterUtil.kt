package com.d103.asaf.common.util

import androidx.recyclerview.widget.DiffUtil
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.MarketImage
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.model.dto.Noti

class AdapterUtil {

    companion object {
        // diffUtil: currentList에 있는 각 아이템들을 비교하여 최신 상태를 유지하도록 한다.
        val diffUtilUserInfo = object : DiffUtil.ItemCallback<Member>() {
            override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
                return oldItem == newItem
            }
        }

        val diffUtilNotiInfo = object : DiffUtil.ItemCallback<Noti>() {
            override fun areItemsTheSame(oldItem: Noti, newItem: Noti): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Noti, newItem: Noti): Boolean {
                return oldItem == newItem
            }
        }

        val diffUtilMarket = object : DiffUtil.ItemCallback<Market>() {
            override fun areItemsTheSame(oldItem: Market, newItem: Market): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Market, newItem: Market): Boolean {
                return oldItem == newItem
            }

        }

        val diffUtilMarketRegister = object : DiffUtil.ItemCallback<MarketImage>() {
            override fun areItemsTheSame(oldItem: MarketImage, newItem: MarketImage): Boolean {
                return oldItem.imageUri == newItem.imageUri
            }

            override fun areContentsTheSame(oldItem: MarketImage, newItem: MarketImage): Boolean {
                return oldItem == newItem
            }

        }
    }
}