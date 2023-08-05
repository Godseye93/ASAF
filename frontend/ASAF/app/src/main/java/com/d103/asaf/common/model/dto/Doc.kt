package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Doc (
    @SerializedName("doc_id") val id: Int = 0, // 책번호
    @SerializedName("class_num") val classNum: Int = 0, // 반ID
    @SerializedName("class_code") val classCode: Int = 0, // 반
    @SerializedName("region_code") val regionCode: Int = 0, // 지역ID
    @SerializedName("generation_code") val generationCode: Int = 0, // 기수ID
    @SerializedName("id") val userId: Int = 0, // 유저ID
    @SerializedName("doc_style") val docStyle: String = "", // 대출자
){
    // 기본 생성자
    //constructor() : this(0, 0, 0, 0, 0, 0, "", "", "", null,null, false,"")
}