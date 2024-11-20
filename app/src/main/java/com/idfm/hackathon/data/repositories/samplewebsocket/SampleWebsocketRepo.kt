package com.idfm.hackathon.data.repositories.samplewebsocket

import okhttp3.OkHttpClient
import okhttp3.Request

class SampleWebsocketRepo {

    private val _client = OkHttpClient()
    private val _request = Request.Builder()
        .url("https://echo.websocket.org/")
        .build()

    private val listener = SampleWebSocketListener()
    private val _webSocket = _client.newWebSocket(_request, listener)

    fun sendText(text: String) {
        _webSocket.send(text)
    }

    fun dispose() {
        _client.dispatcher.executorService.isShutdown
    }
}