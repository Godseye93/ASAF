package com.d103.asaf.common.model.dto

import java.sql.Date


data class Accounts_user(var user_number : Int, var name : String, var email : String,
                         var password : String, var birth_date : Date, var  phone_number : String,
                         var profile_image : String, var electronic_student_id : String, var team_num : String, var authority : String)
