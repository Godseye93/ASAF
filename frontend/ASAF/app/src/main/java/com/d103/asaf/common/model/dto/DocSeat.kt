package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName

data class DocSeat(
    @SerializedName("seat_id") val id: Int = 0,
    @SerializedName("class_num") val classNum: Int = 0,
    @SerializedName("class_code") val classCode: Int = 0,
    @SerializedName("region_code") val regionCode: Int = 0,
    @SerializedName("generation_code") val generationCode: Int = 0,
    @SerializedName("id") val userId: Int = 0,
    @SerializedName("seat_num") var seatNum: Int = 0,
    @SerializedName("name") val name: String = "",
)
