package com.d103.asaf.common.util

import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.api.AttendenceService
import com.d103.asaf.common.model.api.LibraryService
import com.d103.asaf.common.model.api.MemberService
import com.d103.asaf.common.model.api.OpService

class RetrofitUtil {

    companion object{

        val attendenceService = ApplicationClass.retrofit.create(AttendenceService::class.java)
        val memberService = ApplicationClass.retrofit.create(MemberService::class.java)

        // <--------------------- 운영 ---------------------------->
        val opService = ApplicationClass.retrofit.create(OpService::class.java)
        val libraryService = ApplicationClass.retrofit.create(LibraryService::class.java)

        // <--------------------- 도서관 ---------------------------->
    }
}