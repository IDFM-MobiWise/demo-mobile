package com.idfm.hackathon.ui.features.chat

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idfm.hackathon.app.HackathonApp
import com.idfm.hackathon.data.models.ChatMessage
import com.idfm.hackathon.data.repositories.samplewebsocket.WebSocketState
import com.idfm.hackathon.data.repositories.samplewebsocket.WebsocketRepository
import com.idfm.hackathon.ui.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatScreenViewModelImpl(private val _app: HackathonApp,
                              private val _sampleWebsocketRepo: WebsocketRepository
) : BaseViewModel(),
    ChatScreenViewModel {

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(_app)
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
    private var _chatMessages = listOf<ChatMessage>()

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
    }

    private fun setupSpeechRecognizer() {
        _uiState.value = ChatUiState.InProgress(_chatMessages)
        Log.d("ChatScreenViewModelImpl", "setupSpeechRecognizer called")

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _uiState.value = ChatUiState.ResultStt(listOf(""))
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
                _uiState.value = ChatUiState.ErrorStt(error)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.let {
                    // Nothing to do here, just tracing
                    Log.d("ChatScreenViewModelImpl", "onResults")


                    _uiState.value = ChatUiState.ResultStt(it, false)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                // Convert to list of results.
                Log.d("ChatScreenViewModelImpl", "onPartialResults=${matches}")

                matches?.let {
                    _uiState.value = ChatUiState.ResultStt(matches, true)
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
            _uiState.value = ChatUiState.ErrorStt(-1)
        }
    }

    private fun doHandleMessageFromWebsocket(webSocketState: WebSocketState) {
        Log.d("HomeScreenViewModelImpl", "Websocket message received in VM: $webSocketState")
    }

}