package com.d103.asaf.common.model.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NotiMessage")
data class NotiMessage(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val content : String,
    val sendTime : Long,
    val sender : String,
    val senderImage : String,
)
