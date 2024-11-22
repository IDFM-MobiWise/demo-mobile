package com.idfm.hackathon.ui.features.chat

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idfm.hackathon.app.HackathonApp
import com.idfm.hackathon.data.models.ChatMessage
import com.idfm.hackathon.data.models.ChatMessageFromBot
import com.idfm.hackathon.data.models.ChatMessageFromUser
import com.idfm.hackathon.data.models.TransportationLine
import com.idfm.hackathon.data.repositories.samplewebsocket.ReceivedType
import com.idfm.hackathon.data.repositories.samplewebsocket.WebSocketState
import com.idfm.hackathon.data.repositories.samplewebsocket.WebsocketRepository
import com.idfm.hackathon.ui.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class FakeResponse(
    val content: String,
    val transportations: List<TransportationLine> = listOf(),
    val journeyFromDate: String = "",
    val journeyToDate: String = "",
    val remainingTime: String = "",
    val co2: String = "",
    val type: Pair<Boolean, String>
)

class ChatScreenViewModelImpl(
    private val _app: HackathonApp,
    private val _sampleWebsocketRepo: WebsocketRepository
) : BaseViewModel(),
    ChatScreenViewModel {

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(_app)
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }
    private var uid = 1

    private var _chatMessages = listOf<ChatMessage>(
        ChatMessageFromBot(
            uid++,
            Date(),
            listOf("Bonjour, comment puis-je t'aider aujourd'hui ?"),
            listOf(),
            type = Pair(true, "")
        ),
//        ChatMessageFromUser(uid++, Date(), "Je vais à la piscine")
    )

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle(_chatMessages))

    init {
        CoroutineScope(viewModelScope.coroutineContext).launch {
            _sampleWebsocketRepo.stateObserver().collect {
                doHandleMessageFromWebsocket(it)
            }
        }
    }

    override fun uiState(): StateFlow<ChatUiState> {
        return _uiState
    }

    override fun startStt() {
        setupSpeechRecognizer()
    }

    override fun stopStt() {
        TODO("Not yet implemented")
    }

    override fun postUSerRequest(request: String) {
        _sampleWebsocketRepo.sendText(request)

        val tmp = _chatMessages.map { it }.toMutableList()
        tmp.add(ChatMessageFromUser(uid++, Date(), request))

        _chatMessages = tmp.toList()
        _uiState.value = ChatUiState.Response(_chatMessages)
    }

    private fun setupSpeechRecognizer() {
        _uiState.value = ChatUiState.InProgress(_chatMessages)
        Log.d("ChatScreenViewModelImpl", "setupSpeechRecognizer called")

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _uiState.value = ChatUiState.ResultStt(_chatMessages, listOf(""))
            }

            override fun onBeginningOfSpeech() {
                // Nothing to do here, just tracing
                Log.d("ChatScreenViewModelImpl", "onBeginningOfSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Nothing to do here, just tracing
                Log.d("ChatScreenViewModelImpl", "onRmsChanged")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Nothing to do here, just tracing
                Log.d("ChatScreenViewModelImpl", "onBufferReceived")
            }

            override fun onEndOfSpeech() {
                // Nothing to do here, just tracing
                Log.d("ChatScreenViewModelImpl", "onEndOfSpeech")
            }

            override fun onError(error: Int) {
                Log.d("ChatScreenViewModelImpl", "onError")
                _uiState.value = ChatUiState.ErrorStt(_chatMessages, error)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.let {
                    // Nothing to do here, just tracing
                    Log.d("ChatScreenViewModelImpl", "onResults")


                    _uiState.value = ChatUiState.ResultStt(_chatMessages, it, false)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                // Convert to list of results.
                Log.d("ChatScreenViewModelImpl", "onPartialResults=${matches}")

                matches?.let {
                    _uiState.value = ChatUiState.ResultStt(_chatMessages, matches, true)
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Nothing to do here
            }
        })

        doStartListening()
    }

    private fun doStartListening() {
        if (SpeechRecognizer.isRecognitionAvailable(_app)) {
            speechRecognizer.startListening(recognizerIntent)
        } else {
            _uiState.value = ChatUiState.ErrorStt(_chatMessages, -1)
        }
    }

    private fun doHandleMessageFromWebsocket(webSocketState: WebSocketState) {
        Log.d("HomeScreenViewModelImpl", "Websocket message received in VM: $webSocketState")

        when (webSocketState) {
            is WebSocketState.Open -> {
                Log.d("HomeScreenViewModelImpl", "Websocket open")
            }

            is WebSocketState.OnMessage -> {
                Log.d(
                    "HomeScreenViewModelImpl",
                    "Websocket message: ${webSocketState.receivedType}"
                )

                if (webSocketState.receivedType is ReceivedType.Text) {
                    val txt = webSocketState.receivedType.text

                    // Now, deserialize.
                    val gson = Gson()
                    val responseType = object : TypeToken<FakeResponse>() {}.type
                    val response = gson.fromJson<FakeResponse>(txt, responseType)

                    if (response != null) {
                        val tmp = _chatMessages.map { it }.toMutableList()
                        tmp.add(
                            ChatMessageFromBot(
                                uid++,
                                Date(),
                                responseChunks = listOf(response.content),
                                options = listOf(),
                                transportationLines = response.transportations,
                                journeyFrom = response.journeyFromDate,
                                journeyTo = response.journeyToDate,
                                remainingTime = response.remainingTime,
                                co2 = response.co2,
                                type = response.type
                            )
                        )
                        _chatMessages = tmp.toList()
                        _uiState.value = ChatUiState.Response(_chatMessages)
                    }

//                    if (response.node == "respond") {
//                        val msg =
//                            response.values.messages.getOrNull(0)?.kwargs?.lc_kwargs?.lc_kwargs?.content
//
//                        if (msg != null) {
//                            val tmp = _chatMessages.map { it }.toMutableList()
//                            tmp.add(
//                                ChatMessageFromBot(
//                                    uid++,
//                                    Date(),
//                                    responseChunks = listOf(msg),
//                                    options = listOf()
//                                )
//                            )
//                            _chatMessages = tmp.toList()
//                            _uiState.value = ChatUiState.Response(_chatMessages)
//                        }
//                    }
                }
            }

            is WebSocketState.Closing -> {
                Log.d("HomeScreenViewModelImpl", "Websocket closing")
            }

            is WebSocketState.Closed -> {
                Log.d("HomeScreenViewModelImpl", "Websocket closed")
            }

            is WebSocketState.Failure -> {
                Log.d("HomeScreenViewModelImpl", "Websocket failure: ${webSocketState.t}")
            }

            else -> {
                Log.d("HomeScreenViewModelImpl", "Websocket unknown state")
            }
        }
    }

}