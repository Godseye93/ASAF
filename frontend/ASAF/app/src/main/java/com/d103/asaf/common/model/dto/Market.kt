package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName

data class Market(@SerializedName("post_id") var id : Int,
                  @SerializedName("register_time") var registerTime : Long,
                  var title : String,
                  var content : String,
                  @SerializedName("id") var userId : Int,
                  @SerializedName("profile_image") var profileImage : String,
                  @SerializedName("name") var userName : String) {
    constructor(register_time: Long, title : String, content : String, userId : Int, profileImage: String, userName : String) : this(0, register_time, title, content, userId, profileImage, userName)
}
