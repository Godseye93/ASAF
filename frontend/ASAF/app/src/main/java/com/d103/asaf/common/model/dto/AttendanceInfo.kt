package com.d103.asaf.common.model.dto

data class AttendanceInfo(
    val attendanceId: Int,
    val attended: Int,
    val entryTime: Any,
    val exitTime: Any,
    val member: Member
)