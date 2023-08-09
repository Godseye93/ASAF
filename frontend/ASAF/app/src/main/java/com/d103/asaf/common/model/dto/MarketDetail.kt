package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName

data class MarketDetail(
    @SerializedName("post_id") var id : Int,
    @SerializedName("register_time") var registerTime : Long,
    var title : String,
    var content : String,
    @SerializedName("profile_image") var profileImage : String,
    var name : String,
    @SerializedName("id") var userid : Int,
    var images : List<MarketImage>
    )
