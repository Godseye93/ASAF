package com.d103.asaf.common.util

import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.api.AttendenceService

class RetrofitUtil {

    companion object{

        val attendenceService =ApplicationClass.retrofit.create(AttendenceService::class.java)
    }
}