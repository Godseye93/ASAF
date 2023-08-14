package com.d103.asaf.ui.home.pro

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
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.AdapterUtil
import com.d103.asaf.databinding.ItemStudentAttendanceBinding

class UserInfoAdapter(var context : Context) : ListAdapter<Member, UserInfoAdapter.ItemViewHolder>(
    AdapterUtil.diffUtilUserInfo) {




    inner class ItemViewHolder(var binding : ItemStudentAttendanceBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(user : Member){
            Log.d("유저 프로필", "${ApplicationClass.API_URL}member/${user.profileImage.split("/")[6].split(".")[0]}.com/profile-image")
            Log.d("유저 프로필", "${ApplicationClass.API_URL}${user.profileImage}")
            Log.d("유저 프로필", "${ApplicationClass.API_URL}${user.profileImage}")
            binding.userName.text = user.memberName

            val imageSplit = user.profileImage.split("/")
            val path =  "http://i9d103.p.ssafy.io" + "/" + imageSplit[4] + "/" + imageSplit[5] + "/" + imageSplit[6]
            Glide.with(context).load(path).into(binding.userProfileImage)
            if(user.attended == "미출석"){
                binding.attendanceCheckImage.setBackgroundColor(Color.RED)
            }
            else if(user.attended == "입실"){
                binding.attendanceCheckImage.setBackgroundColor(Color.GREEN)
            }
            else {
                binding.attendanceCheckImage.setBackgroundColor(Color.YELLOW)
            }
            binding.checkBox.setOnClickListener {
                if (it is CheckBox) {
                    val isChecked = it.isChecked // CheckBox의 선택 여부를 확인함
                    itemClickListener.onClick(it, layoutPosition, user, isChecked)
                }

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
