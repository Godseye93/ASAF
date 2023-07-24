package com.d103.asaf.common.model.dto

import java.sql.Date


data class Accounts_user(var userNumber : Int, var name : String, var email : String,
                         var password : String, var birthDate : Date, var  phoneNumber : String,
                         var profileImage : String, var electronicStudentId : String, var teamNum : String, var authority : String)
