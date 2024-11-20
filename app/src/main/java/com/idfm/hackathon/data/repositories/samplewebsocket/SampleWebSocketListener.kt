package com.idfm.hackathon.data.repositories.samplewebsocket

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

sealed class ReceivedType {
    data class Text(val text: String) : ReceivedType()
    data class Bytes(val bytes: ByteString) : ReceivedType()
}

sealed class WebSocketState {
    data object Void : WebSocketState()
    data object Open : WebSocketState()
    data class OnMessage(val receivedType: ReceivedType) : WebSocketState()
    data class Closing(val code: Int, val reason: String) : WebSocketState()
    data class Closed(val code: Int, val reason: String) : WebSocketState()
    data class Failure(val t: Throwable, val response: Response?) : WebSocketState()
}

class SampleWebSocketListener : WebSocketListener() {

    private val _webSocketState = MutableStateFlow<WebSocketState>(WebSocketState.Void)

    val webSocketState: StateFlow<WebSocketState> = _webSocketState

    override fun onOpen(webSocket: WebSocket, response: Response) {
        _webSocketState.value = WebSocketState.Open
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("SampleWebSocket", "Received message: $text")
        _webSocketState.value = WebSocketState.OnMessage(ReceivedType.Text(text))
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("SampleWebSocket", "Received bytes: $bytes")
        _webSocketState.value = WebSocketState.OnMessage(ReceivedType.Bytes(bytes))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        Log.d("SampleWebSocket", "WebSocket closing: $code / $reason")
        _webSocketState.value = WebSocketState.Closing(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("SampleWebSocket", "WebSocket closed: $code / $reason")
        _webSocketState.value = WebSocketState.Closed(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
        _webSocketState.value = WebSocketState.Failure(t, response)
    }
}