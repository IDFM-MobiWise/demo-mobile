package com.idfm.hackathon.data.repositories.samplewebsocket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FakeWebsocketRepoImpl() : WebsocketRepository {

    private val _state = MutableStateFlow<WebSocketState>(WebSocketState.Void)

    override fun stateObserver(): StateFlow<WebSocketState> {
        return _state
    }

    override fun sendText(text: String) {
        doEmit(
            listOf(
                WebSocketState.Open,
                WebSocketState.OnMessage(ReceivedType.Text("You said: '$text'")),
                WebSocketState.OnMessage(ReceivedType.Text("Am happy to hear that")),
                WebSocketState.OnMessage(ReceivedType.Text("Let me help you boss"))
            )
        )
    }

    override fun dispose() {
        // Nothing to do, really
    }

    private fun doEmit(states: List<WebSocketState>) {
        CoroutineScope(Dispatchers.IO).launch {
            states.forEach {
                _state.emit(it)
                delay(1000)
            }
        }
    }

}