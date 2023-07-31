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
    @SerializedName("book_name") val bookName: String = "", // 책 제목
    @SerializedName("author") val author: String = "", // 작가
    @SerializedName("publisher") val publisher: String = "", // 출판사
    @SerializedName("borrowdate") val borrowDate: Date = Date(), // 대출일
    @SerializedName("returndate") val returnDate: Date = Date(), // 반납일
    @SerializedName("borrow_state") val borrowState: Boolean = false, // 대출 상태
    @SerializedName("borrower") val borrower: String = "", // 대출자
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
        Date(parcel.readLong()),
        Date(parcel.readLong()),
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
        parcel.writeLong(borrowDate.time)
        parcel.writeLong(returnDate.time)
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
