package com.idfm.hackathon.ui.features.home

import com.idfm.hackathon.ui.nav.ToolbarController
import kotlinx.coroutines.flow.StateFlow

sealed class HomeUiState {
    data object Idle : HomeUiState()
    data object InitialisingStt : HomeUiState()
    data class Listening(val textList: List<String>) : HomeUiState()
    data object ProcessingStt : HomeUiState()
    data class ErrorStt(val errorCode: Int) : HomeUiState()
}


interface HomeScreenViewModel : ToolbarController {
    fun uiState(): StateFlow<HomeUiState>
    fun startStt()
    fun stopStt()
}