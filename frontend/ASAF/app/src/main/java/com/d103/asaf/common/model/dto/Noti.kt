package com.d103.asaf.common.model.dto

import java.util.Date

data class Noti(var id : Int, var date : Date, var title : String, var notiDetail : String, var notification : Boolean, var userid : Int )
