package com.idfm.hackathon.data.models

import android.os.Parcel
import android.os.Parcelable

sealed class ChatMessage : Parcelable {
    data class FromBot(
        val responseChunks: List<String>,
        val options: List<String>
    ) : ChatMessage() {
        constructor(parcel: Parcel) : this(
            parcel.createStringArrayList() ?: listOf(),
            parcel.createStringArrayList() ?: listOf()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeStringList(responseChunks)
            parcel.writeStringList(options)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<FromBot> {
            override fun createFromParcel(parcel: Parcel): FromBot = FromBot(parcel)
            override fun newArray(size: Int): Array<FromBot?> = arrayOfNulls(size)
        }
    }

    data class FromUser(val message: String) : ChatMessage() {
        constructor(parcel: Parcel) : this(parcel.readString() ?: "")

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(message)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<FromUser> {
            override fun createFromParcel(parcel: Parcel): FromUser = FromUser(parcel)
            override fun newArray(size: Int): Array<FromUser?> = arrayOfNulls(size)
        }
    }
}