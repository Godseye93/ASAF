package com.d103.asaf.common.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DocSign(
    @SerializedName("sign_id") val id: Int = 0,
    @SerializedName("class_num") val classNum: Int = 0,
    @SerializedName("class_code") val classCode: Int = 0,
    @SerializedName("region_code") val regionCode: Int = 0,
    @SerializedName("generation_code") val generationCode: Int = 0,
    @SerializedName("id") val userId: Int = 0,
    @SerializedName("image_url") val imageUrl: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("month") val month: String = "",
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(classNum)
        parcel.writeInt(classCode)
        parcel.writeInt(regionCode)
        parcel.writeInt(generationCode)
        parcel.writeInt(userId)
        parcel.writeString(imageUrl)
        parcel.writeString(name)
        parcel.writeString(month)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DocSign> {
        override fun createFromParcel(parcel: Parcel): DocSign {
            return DocSign(parcel)
        }

        override fun newArray(size: Int): Array<DocSign?> {
            return arrayOfNulls(size)
        }
    }
}
