package com.idfm.hackathon.ui.features.chat

import com.idfm.hackathon.data.models.ChatMessage
import com.idfm.hackathon.ui.nav.ToolbarController
import kotlinx.coroutines.flow.StateFlow

sealed class ChatUiState(val messages: List<ChatMessage>) {
    data class Idle(private val _messages: List<ChatMessage>) : ChatUiState(_messages)
    data class InProgress(private val _messages: List<ChatMessage>) : ChatUiState(_messages)
    data class Response(private val _messages: List<ChatMessage>) : ChatUiState(_messages)
    data class ResultStt(private val _messages: List<ChatMessage>, val textList: List<String>, val partial: Boolean = false) : ChatUiState(_messages)
    data class ErrorStt(private val _messages: List<ChatMessage>, val errorCode: Int) : ChatUiState(_messages)
}


interface ChatScreenViewModel : ToolbarController {
    fun uiState(): StateFlow<ChatUiState>
    fun startStt()
    fun stopStt()
    fun postUSerRequest(request: String)
}