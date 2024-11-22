package com.idfm.hackathon.data.models

import java.util.Date

//sealed class ChatMessage(val id: Int) : Parcelable {
//    data class FromBot(
//        val lid: Int,
//        val responseChunks: List<String>,
//        val options: List<String>
//    ) : ChatMessage(lid) {
//        constructor(parcel: Parcel) : this(
//            parcel.createIntArray(),
//            parcel.createStringArrayList() ?: listOf(),
//            parcel.createStringArrayList() ?: listOf()
//        )
//
//        override fun writeToParcel(parcel: Parcel, flags: Int) {
//            parcel.writeInt(lid)
//            parcel.writeStringList(responseChunks)
//            parcel.writeStringList(options)
//        }
//
//        override fun describeContents(): Int = 0
//
//        companion object CREATOR : Parcelable.Creator<FromBot> {
//            override fun createFromParcel(parcel: Parcel): FromBot = FromBot(parcel)
//            override fun newArray(size: Int): Array<FromBot?> = arrayOfNulls(size)
//        }
//    }
//
//    data class FromUser(val message: String) : ChatMessage() {
//        constructor(parcel: Parcel) : this(parcel.readString() ?: "")
//
//        override fun writeToParcel(parcel: Parcel, flags: Int) {
//            parcel.writeString(message)
//        }
//
//        override fun describeContents(): Int = 0
//
//        companion object CREATOR : Parcelable.Creator<FromUser> {
//            override fun createFromParcel(parcel: Parcel): FromUser = FromUser(parcel)
//            override fun newArray(size: Int): Array<FromUser?> = arrayOfNulls(size)
//        }
//    }
//}

open class ChatMessage(val id: Int, val timeStamp: Date)

class ChatMessageFromUser(id: Int, timeStamp: Date, val message: String) :
    ChatMessage(id, timeStamp)

class ChatMessageFromBot(
    id: Int,
    timeStamp: Date,
    val responseChunks: List<String>,
    val options: List<String>
) : ChatMessage(id, timeStamp)