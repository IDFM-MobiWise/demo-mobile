package com.idfm.hackathon.data.repositories.samplewebsocket

import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.Request

class SampleWebsocketRepoImpl: WebsocketRepository {

    private val _client = OkHttpClient()
    private val _request = Request.Builder()
        //.url("https://echo.websocket.org/")
        .url("https://echo.websocket.org/.ws")
//        .url("wss://socketsbay.com/wss/v2/10/98da936e4082c8839a5b8cb4789acb2c")
        .build()

    private val _listener = SampleWebSocketListener()
    private val _webSocket = _client.newWebSocket(_request, _listener)

    override fun stateObserver(): StateFlow<WebSocketState> {
        return _listener.webSocketState
    }

    override fun sendText(text: String) {
        _webSocket.send(text)
    }

    override fun dispose() {
        _client.dispatcher.executorService.isShutdown
    }
}