package com.d103.asaf.common.model.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Member(
    val id: Int,
    @SerializedName("student_number") val studentNumber: Int,
    val memberName: String,
    val memberEmail: String,
    val memberPassword: String,
    @SerializedName("birth_date") val birthDate: Date,
    @SerializedName("electronic_student_id") val electronicStudentId: Int,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("profile_image") val profileImage: String,
    @SerializedName("team_num") val teamNum: Int,
    val authority: String,
)