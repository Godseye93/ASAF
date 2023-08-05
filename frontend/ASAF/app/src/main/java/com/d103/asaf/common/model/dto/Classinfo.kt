package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName

data class Classinfo(
    @SerializedName("class_num")var classNum : Int,
    @SerializedName("class_code")var classCode : Int,
    @SerializedName("region_code")var regionCode : Int,
    @SerializedName("generation_code")var generationCode : Int,
    @SerializedName("id")var userId : Int)
