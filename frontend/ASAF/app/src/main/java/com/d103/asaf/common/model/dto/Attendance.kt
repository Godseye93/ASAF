package com.d103.asaf.common.model.dto

import java.sql.Time

data class Attendance(var user : Accounts_user, var attendanceId : Int, var userId : Int, var attendanceStart : Time, var attendanceEnd : Time, var attended : String)
