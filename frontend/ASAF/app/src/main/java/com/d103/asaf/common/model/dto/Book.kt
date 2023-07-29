package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName
import java.util.Calendar
import java.util.Date

data class Book (
    @SerializedName("book_number") val id: Int = 0, // 책번호
    @SerializedName("class_num") val classNum: Int = 0, // 반ID
    @SerializedName("class_code") val classCode: Int = 0, // 반
    @SerializedName("region_code") val regionCode: Int = 0, // 지역ID
    @SerializedName("generation_code") val generationCode: Int = 0, // 기수ID
    @SerializedName("id") val userId: Int = 0, // 유저ID
    @SerializedName("book_name") val bookName: String = "", // 책 제목
    @SerializedName("author") val author: String = "", // 작가
    @SerializedName("publisher") val publisher: String = "", // 출판사
    @SerializedName("borrowdate") val borrowDate: Date = Date(), // 대출일
    @SerializedName("returndate") val returnDate: Date = Date(), // 반납일
    @SerializedName("borrow_state") val borrowState: Boolean = false, // 대출 상태
    @SerializedName("borrower") val borrower: String = "", // 대출자
){
    // 기본 생성자
    //constructor() : this(0, 0, 0, 0, 0, 0, "", "", "", null,null, false,"")
}
