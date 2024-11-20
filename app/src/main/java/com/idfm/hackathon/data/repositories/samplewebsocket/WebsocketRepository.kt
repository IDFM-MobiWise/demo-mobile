package com.idfm.hackathon.data.repositories.samplewebsocket

import kotlinx.coroutines.flow.StateFlow

interface WebsocketRepository {

    fun stateObserver(): StateFlow<WebSocketState>
    fun sendText(text: String)
    fun dispose()
}