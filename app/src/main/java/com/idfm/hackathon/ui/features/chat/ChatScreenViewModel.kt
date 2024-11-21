package com.idfm.hackathon.ui.features.chat

import com.idfm.hackathon.data.models.ChatMessage
import com.idfm.hackathon.ui.nav.ToolbarController
import kotlinx.coroutines.flow.StateFlow

sealed class ChatUiState {
    data object Idle : ChatUiState()
    data class InProgress(val currentMessages: List<ChatMessage>) : ChatUiState()
    data class Response(val responses: List<ChatMessage>) : ChatUiState()
    data class ResultStt(val textList: List<String>, val partial: Boolean = false) : ChatUiState()
    data class ErrorStt(val errorCode: Int) : ChatUiState()
}


interface ChatScreenViewModel : ToolbarController {
    fun uiState(): StateFlow<ChatUiState>
    fun startStt()
    fun stopStt()
    fun postUSerRequest(request: String)
}