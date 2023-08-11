package com.d103.asaf.common.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Calendar
import java.util.Date

data class Book (
    @SerializedName("book_number") val id: Int = 0, // 책번호
    @SerializedName("class_num") val classNum: Int = 0, // 반ID
    @SerializedName("class_code") val classCode: Int = 0, // 반
    @SerializedName("region_code") val regionCode: Int = 0, // 지역ID
    @SerializedName("generation_code") val generationCode: Int = 0, // 기수ID
    @SerializedName("id") val userId: Int = 0, // 유저ID
    @SerializedName("bookName") val bookName: String = "", // 책 제목
    @SerializedName("author") val author: String = "", // 작가
    @SerializedName("publisher") val publisher: String = "", // 출판사
    @SerializedName("borrowDate") val borrowDate: Long = Long.MAX_VALUE, // 대출일
    @SerializedName("returnDate") val returnDate: Long = Long.MAX_VALUE, // 반납일
    @SerializedName("borrowState") var borrowState: Boolean = false, // 대출 상태
    @SerializedName("borrower") val borrower: String? = "", // 대출자
    @SerializedName("bookNameCount") val bookNameCount: Int = 0, // 총 수량
    @SerializedName("trueBorrowStateCount") val trueBorrowStateCount: Int = 0 // 빌린 수량
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(classNum)
        parcel.writeInt(classCode)
        parcel.writeInt(regionCode)
        parcel.writeInt(generationCode)
        parcel.writeInt(userId)
        parcel.writeString(bookName)
        parcel.writeString(author)
        parcel.writeString(publisher)
        parcel.writeLong(borrowDate)
        parcel.writeLong(returnDate)
//        borrowDate?.let { parcel.writeLong(it) } ?: parcel.writeLong(Long.MAX_VALUE) // Nullable Long 값 쓰기
//        returnDate?.let { parcel.writeLong(it) } ?: parcel.writeLong(Long.MAX_VALUE)
        parcel.writeByte(if (borrowState) 1 else 0)
        parcel.writeString(borrower)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
