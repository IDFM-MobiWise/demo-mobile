package com.idfm.hackathon.ui.features.home

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.idfm.hackathon.app.HackathonApp
import com.idfm.hackathon.data.models.SampleDto
import com.idfm.hackathon.data.repositories.RepositoryResult
import com.idfm.hackathon.data.repositories.sample.SampleRepository
import com.idfm.hackathon.data.repositories.samplewebsocket.FakeWebsocketRepoImpl
import com.idfm.hackathon.data.repositories.samplewebsocket.SampleWebsocketRepoImpl
import com.idfm.hackathon.data.repositories.samplewebsocket.WebSocketState
import com.idfm.hackathon.data.repositories.samplewebsocket.WebsocketRepository
import com.idfm.hackathon.ui.BaseViewModel
import com.idfm.hackathon.ui.nav.ActionMenuItem
import com.idfm.hackathon.ui.nav.MenuItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeScreenViewModelImpl(
    private val _app: HackathonApp,
    private val sampleRepo: SampleRepository
) : BaseViewModel(),
    HomeScreenViewModel {
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(_app)
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    private val _sampleData = MutableStateFlow<Result<SampleDto>?>(null)
    val sampleData: StateFlow<Result<SampleDto>?> = _sampleData

    private val _sampleWebsocketRepoImpl: WebsocketRepository = FakeWebsocketRepoImpl() // SampleWebsocketRepoImpl()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)

    init {
        setToolbarItems(listOf(menuItemStopStt()))

        CoroutineScope(viewModelScope.coroutineContext).launch {
            _sampleWebsocketRepoImpl.stateObserver().collect {
                doHandleMessageFromWebsocket(it)

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
        _sampleWebsocketRepoImpl.dispose()
    }

    override fun uiState(): StateFlow<HomeUiState> {
        return _uiState
    }

    override fun onMenuItemClick(item: ActionMenuItem) {
        when (item.key) {
            "mic_on_true", "mic_on_false" -> stopStt()
            "mic_off" -> startStt()
        }
    }

    override fun startStt() {
        setupSpeechRecognizer()
    }

    override fun stopStt() {
        speechRecognizer.stopListening()
    }

    override fun fetchStuff() {
        fetchData()
    }

    override fun sendToWebsocket(text: String) {
        doSendToWebsocket(text)
    }

    private fun fetchData() {
        viewModelScope.launch {
            sampleRepo.fetchData().collect { result ->
                _uiState.value = if (result is RepositoryResult.Loading)
                    HomeUiState.InProgress
                else
                    HomeUiState.Idle

                Log.d("HomeScreenViewModelImpl", "fetchData result=${result}")
            }
        }
    }

    private fun menuItemStartStt(active: Boolean): ActionMenuItem {
        return MenuItems.menuMicOn(active)
    }

    private fun menuItemStopStt(): ActionMenuItem {
        return MenuItems.menuMicOff()
    }

    private fun setupSpeechRecognizer() {
        _uiState.value = HomeUiState.InProgress
        Log.d("HomeScreenViewModelImpl", "setupSpeechRecognizer called")

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                setToolbarItems(listOf(menuItemStartStt(true)))
                _uiState.value = HomeUiState.ResultStt(listOf(""))
            }

            override fun onBeginningOfSpeech() {
                // Nothing to do here, just tracing
                Log.d("HomeScreenViewModelImpl", "onBeginningOfSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Nothing to do here, just tracing
                Log.d("HomeScreenViewModelImpl", "onRmsChanged")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Nothing to do here, just tracing
                Log.d("HomeScreenViewModelImpl", "onBufferReceived")
            }

            override fun onEndOfSpeech() {
                // Nothing to do here, just tracing
                Log.d("HomeScreenViewModelImpl", "onEndOfSpeech")
            }

            override fun onError(error: Int) {
                Log.d("HomeScreenViewModelImpl", "onError")
                setToolbarItems(listOf(menuItemStopStt()))
                _uiState.value = HomeUiState.ErrorStt(error)
            }

            override fun onResults(results: Bundle?) {
                setToolbarItems(listOf(menuItemStopStt()))
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.let {
                    // Nothing to do here, just tracing
                    Log.d("HomeScreenViewModelImpl", "onResults")

//                    val recognizedText = it[0]
//                    viewModelScope.launch {
//                        // Process the recognized text
//                    }

                    _uiState.value = HomeUiState.ResultStt(it, false)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                // Convert to list of results.
                Log.d("HomeScreenViewModelImpl", "onPartialResults=${matches}")

                matches?.let {
                    _uiState.value = HomeUiState.ResultStt(matches, true)
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
            _uiState.value = HomeUiState.ErrorStt(-1)
        }
    }


    private fun doSendToWebsocket(text: String) {
        Log.d("HomeScreenViewModelImpl", "sending to websocket result=$text")
        _sampleWebsocketRepoImpl.sendText(text)
    }

    private fun doHandleMessageFromWebsocket(webSocketState: WebSocketState) {
        Log.d("HomeScreenViewModelImpl", "Websocket message received in VM: $webSocketState")
    }
}