package com.idfm.hackathon.data.repositories.samplewebsocket

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class SampleWebsocketRepo {

    private val _client = OkHttpClient()
    private val _request = Request.Builder()
        //.url("https://echo.websocket.org/")
        .url("https://echo.websocket.org/.ws")
//        .url("wss://socketsbay.com/wss/v2/10/98da936e4082c8839a5b8cb4789acb2c")
        .build()

    private val _listener = SampleWebSocketListener()
    private val _webSocket = _client.newWebSocket(_request, _listener)

//    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            _listener.webSocketState.collect {
//                Log.d("SampleWebsocketRepo", "WebSocket state as seen in repo: $it")
//            }
//        }
//    }

    fun stateObserver(): StateFlow<WebSocketState> {
        return _listener.webSocketState
    }

    fun sendText(text: String) {
        _webSocket.send(text)
    }

    fun dispose() {
        _client.dispatcher.executorService.isShutdown
    }
}