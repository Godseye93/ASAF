package com.d103.asaf.common.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DocLocker (
    @SerializedName("locker_id") val id: Int = 0,
    @SerializedName("doc_id") val docId: Int = 0,
    @SerializedName("class_num") val classNum: Int = 0,
    @SerializedName("class_code") val classCode: Int = 0,
    @SerializedName("region_code") val regionCode: Int = 0,
    @SerializedName("generation_code") val generationCode: Int = 0,
    @SerializedName("id") val userId: Int = 0,
    @SerializedName("locker_num") var lockerNum: Int = 0,
    @SerializedName("name") val name: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(docId)
        parcel.writeInt(classNum)
        parcel.writeInt(classCode)
        parcel.writeInt(regionCode)
        parcel.writeInt(generationCode)
        parcel.writeInt(userId)
        parcel.writeInt(lockerNum)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DocLocker> {
        override fun createFromParcel(parcel: Parcel): DocLocker {
            return DocLocker(parcel)
        }

        override fun newArray(size: Int): Array<DocLocker?> {
            return arrayOfNulls(size)
        }
    }
}