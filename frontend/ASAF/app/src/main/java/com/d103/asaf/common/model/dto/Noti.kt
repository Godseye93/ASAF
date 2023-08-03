package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Noti(var id : Int,
                var title : String,
                var content : String,
                @SerializedName("registerTime") var registerTime : Long,
                @SerializedName("send_time")var sendTime : Long,
                var sender : Int,
                var receiver : Int,
                var notification : Boolean){
    constructor() : this(0, "", "", System.currentTimeMillis(), System.currentTimeMillis(), 0, 0, false)
}
