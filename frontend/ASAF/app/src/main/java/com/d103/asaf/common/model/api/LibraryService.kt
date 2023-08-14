package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.common.model.dto.DocSeat
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface LibraryService {
    // 모든 도서
    @GET("/book/distinct/{class_code}/{region_code}/{generation_code}")
    suspend fun getBooks(@Path("class_code") classCode : Int, @Path("region_code") regionCode : Int, @Path("generation_code") generationCode : Int) : Response<MutableList<Book>>

    // 대출 중인 도서
    @GET("/book/borrowed/{class_code}/{region_code}/{generation_code}/sorted-by-name")
    suspend fun getDraws(@Path("class_code") classCode : Int, @Path("region_code") regionCode : Int, @Path("generation_code") generationCode : Int) : Response<MutableList<Book>>

//    // 내가 대출 중인 도서
//    @GET("/book/borrowed/{class_code}/{region_code}/{generation_code}/{user_id}/sorted-by-name")
//    suspend fun getMyDraws(@Path("class_code") classCode : Int, @Path("region_code") regionCode : Int, @Path("generation_code") generationCode : Int,@Path("user_id") userId: Int) : Response<MutableList<Book>>

    // 내가 대출 중인 도서
    @GET("/book/borrowed/user/{user_id}")
    suspend fun getMyDraws(@Path("user_id") userId : Int) : Response<MutableList<Book>>


    // 한권 정보만 가져오기
    @GET("/book/{book_id}")
    suspend fun getBook(@Path("book_id") bookId: Int) : Response<Book>

    // 동일 제목 / 저자인 책 정보 가져오기 (서버 쪽에 요청해보기 수량 정보를 별도의 변수에 담아서 줘야할 듯? 굳이 DB 컬럼은 없어도 됨)
    // BookDto도 변해야함 count 정보 추가
    // 그러면 아래 함수도 별도로 필요없음
    @GET("/library/return/{title}/{author}")
    suspend fun getBookCounts(@Path("title") title : String, @Path("author") author:String) : Int

    // 도서 등록 정보 보내기
    @POST("/book")
    suspend fun postBook(@Body book: Book) : Response<Book>

    // 반납 정보 보내기
    @POST("/library/seat/{book_id}")
    suspend fun postReturn(@Body book: Book) : Response<Boolean>

    // 대출 정보 보내기
    @POST("/library/seat/{book_id}")
    suspend fun postDraw(@Body book: Book) : Response<Boolean>

    // 수정
    @PUT("/book/{book_id}")
    suspend fun updateBook(@Path("book_id") bookId: Int) : Response<Boolean>

    // 대출(반납) 수정
    @PUT("/book/borrow/{book_number}")
    suspend fun updateDrawBook(@Path("book_number") bookId: Int, @Body book: Book) : Response<Book>

    // 삭제
    @DELETE("/book/{book_id}")
    suspend fun deleteBook(@Path("book_id") bookId: Int) : Response<Boolean>
}