package com.idfm.hackathon.data.models

sealed class ChatMessage {
    data class FromUser(val request: String) : ChatMessage()
    data class FromBot(val responseChunks: List<String>, val options: List<String>) : ChatMessage()
}