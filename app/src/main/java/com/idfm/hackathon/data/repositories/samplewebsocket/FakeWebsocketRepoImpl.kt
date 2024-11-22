package com.idfm.hackathon.data.repositories.samplewebsocket

import com.google.gson.Gson
import com.idfm.hackathon.data.models.TransportationLine
import com.idfm.hackathon.ui.features.chat.FakeResponse
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
//                WebSocketState.Open,
                WebSocketState.OnMessage(ReceivedType.Text(
                    FakeResponse(
                        content = "ok, pour prendre le TGV à la Gare de Lyon à 17:04, et arriver arriver avec 20 minutes d'avance, " +
                                "il faut partir de la Montparnasse à 16:28. Le traffic est actuellement fluide, et la l'affluence habituelle. ",
                        listOf(TransportationLine.METRO_4, TransportationLine.METRO_14),
                        journeyFromDate = "16:28",
                        journeyToDate = "16:44",
                        remainingTime = "16 min",
                        co2 = "15 gr (CO2)"

                    ).let {
                        Gson().toJson(it)
                    }
                )),

                WebSocketState.OnMessage(ReceivedType.Text(
                    FakeResponse(
                        content = "Un incident est survenu sur la ligne 14, je te propose la meilleure " +
                                "alternative, et de changer à Châtelet pour prendre la ligne 1, direction Château de Vincennes. Pas d'inquiétude, tu auras toujours 15 minutes d'avance pour ton TGV.",
                        listOf(TransportationLine.METRO_4, TransportationLine.METRO_1),
                        journeyFromDate = "16:37",
                        journeyToDate = "16:49",
                        remainingTime = "12 min",
                        co2 = "8 gr (CO2)"
                    ).let {
                        Gson().toJson(it)
                    }
                ))
            ),

            )
    }

    override fun dispose() {
        // Nothing to do, really
    }

    private fun doEmit(states: List<WebSocketState>) {
        CoroutineScope(Dispatchers.IO).launch {
            states.forEach {
                delay(2000)
                _state.emit(it)
                delay(5000)
            }
        }
    }

}