package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.common.model.dto.DocSeat
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LibraryService {
    // 모든 도서
    @GET("/library/{classCode}")
    suspend fun getBooks(@Path("classCode") classCode : Int) : Response<MutableList<Book>>

    // 대출 중인 도서
    @GET("/library/return/{classCode}")
    suspend fun getDraws(@Path("classCode") classCode : Int) : Response<MutableList<Book>>

    // 동일 제목 / 저자인 책 정보 가져오기 (서버 쪽에 요청해보기 수량 정보를 별도의 변수에 담아서 줘야할 듯? 굳이 DB 컬럼은 없어도 됨)
    // BookDto도 변해야함 count 정보 추가
    // 그러면 아래 함수도 별도로 필요없음
    @GET("/library/return/{title}/{author}")
    suspend fun getBookCounts(@Path("title") title : String, @Path("author") author:String) : Int

    // 도서 등록 정보 보내기
    @POST("/library/seat")
    suspend fun postBook(@Body book: Book) : Boolean

    // 반납 정보 보내기
    @POST("/library/seat/{book_id}")
    suspend fun postReturn(@Body book: Book) : Boolean

    // 대출 정보 보내기
    @POST("/library/seat/{book_id}")
    suspend fun postDraw(@Body book: Book) : Boolean
}