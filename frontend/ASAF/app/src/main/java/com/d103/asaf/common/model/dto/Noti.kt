package com.d103.asaf.common.model.dto

import java.util.Date

data class Noti(var id : Int, var title : String,  var content : String, var registerTime : Date, var sendTime : Date, var sender : Int, var receiver : Int , var notification : Boolean){
    constructor() : this(0, "", "", Date(System.currentTimeMillis()), Date(System.currentTimeMillis()), 0, 0, false)
}
