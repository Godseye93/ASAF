package com.d103.asaf.ui.op.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.databinding.ItemMoneyBinding
import com.d103.asaf.ui.op.dialog.CustomImageDialog

// String -> MoneyDto 로 교체 (주소 String / 이름 String / 유저ID / MONTH 정보 가짐) - Userid로 본인의 제출 여부 확인
class MoneyAdapter : androidx.recyclerview.widget.ListAdapter<DocSign, MoneyAdapter.MoneyViewHolder>(MoneyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMoneyBinding.inflate(inflater, parent, false)
        return MoneyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoneyViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

    inner class MoneyViewHolder(private val binding: ItemMoneyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sign: DocSign) {
            val realImg = "http://i9d103.p.ssafy.io/${sign.imageUrl}"
            // 이미지 누르면 확대되는 기능 set
            // 이미지를 money리스트 정보에서 가져오기
            binding.apply {
                itemMoneyImageviewImage.setOnClickListener {
                    CustomImageDialog(root.context, realImg).show()
                }
                Glide.with(binding.root)
                    .load(realImg)
                    .into(itemMoneyImageviewImage)
                itemMoneyTextviewText.text = sign.name
            }

        }
    }

    class MoneyDiffCallback : DiffUtil.ItemCallback<DocSign>() {
        override fun areItemsTheSame(oldItem: DocSign, newItem: DocSign): Boolean {
            // 식별자 요소를 비교하는게 맞다
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DocSign, newItem: DocSign): Boolean {
            return oldItem == newItem
        }
    }
}