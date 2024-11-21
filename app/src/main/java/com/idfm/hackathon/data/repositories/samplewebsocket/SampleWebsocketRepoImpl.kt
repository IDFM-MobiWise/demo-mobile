package com.idfm.hackathon.data.repositories.samplewebsocket

import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.Request

class SampleWebsocketRepoImpl : WebsocketRepository {

    private val _client = OkHttpClient()
    private val _request = Request.Builder()
        // .url("https://echo.websocket.org/.ws")
        .url("ws://user-arthursamy-46447-0.data-platform-self-service.net/proxy/9998/")
        .addHeader(
            "Cookie",
            "session=%24argon2id%24v%3D19%24m%3D65536%2Ct%3D3%2Cp%3D4%244XtuxwNYdTy%2F40pP3XoxXQ%24lbHpzmgDqjslb2QFEb0D3hlhbqEjsx1JiIuyXXJCUV0"
        )
        .build()

    private val _listener = SampleWebSocketListener()
    private val _webSocket = _client.newWebSocket(_request, _listener)

    override fun stateObserver(): StateFlow<WebSocketState> {
        return _listener.webSocketState
    }


    override fun sendText(text: String) {
        val msg = "{\"userMessage\": \"$text\"}"
        _webSocket.send(msg)
    }

    override fun dispose() {
        _client.dispatcher.executorService.isShutdown
    }
}