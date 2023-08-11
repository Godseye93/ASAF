package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.util.Date

data class Member(
    val id: Int,
    @SerializedName("student_number") var studentNumber: Int,
    var memberName: String,
    val memberEmail: String,
    var memberPassword: String,
//    val memberDate: String,
    @SerializedName("member_info") var memberInfo: String,
    @SerializedName("birth_date") var birthDate: String,
    @SerializedName("electronic_student_id") var electronicStudentId: Int,
    @SerializedName("phone_number") var phoneNumber: String,
    @SerializedName("profile_image") var profileImage: String,
    @SerializedName("team_num") var teamNum: Int,
    var token : String,
    var attended : String,
    var entryTime : Long?,
    var exitTime : Long?,
    var authority: String = "교육생",
){
    // 기본 생성자
    constructor() : this(0, 0, "", "", "", "", "",
        0, "", "", 0, "", "", null, null, "교육생")

    // 추가 생성자 추가
    constructor(
        studentNumber: Int,
        memberName: String,
        memberEmail: String,
        memberPassword: String,
        birthDate: String,
        memberInfo: String,
        phoneNumber: String,
    ) : this(0, studentNumber, memberName, memberEmail,
        memberPassword, memberInfo, birthDate, 0, phoneNumber, "", 0, "", "", null, null, "교육생")

    constructor(
        memberEmail: String,
        memberPassword: String
    ) : this(0, 0, "", memberEmail,
        memberPassword, "", "", 0, "", "", 0, "", "", null, null, "교육생")
}