package com.idfm.hackathon.ui.features.home

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.idfm.hackathon.app.HackathonApp
import com.idfm.hackathon.ui.BaseViewModel
import com.idfm.hackathon.ui.nav.ActionMenuItem
import com.idfm.hackathon.ui.nav.MenuItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class HomeScreenViewModelImpl(private val _app: HackathonApp) : BaseViewModel(),
    HomeScreenViewModel {
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(_app)
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Idle)

    init {
        setToolbarItems(listOf(menuItemStopStt()))
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer.destroy()
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
        TODO("Not yet implemented")
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
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

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
}