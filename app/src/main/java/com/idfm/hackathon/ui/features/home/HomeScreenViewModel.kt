package com.idfm.hackathon.ui.features.home

import com.idfm.hackathon.ui.nav.ToolbarController
import kotlinx.coroutines.flow.StateFlow

sealed class HomeUiState {
    data object Idle : HomeUiState()
    data object InProgress : HomeUiState()
    data class ResultStt(val textList: List<String>, val partial: Boolean = false) : HomeUiState()
    data class ErrorStt(val errorCode: Int) : HomeUiState()
}


interface HomeScreenViewModel : ToolbarController {
    fun uiState(): StateFlow<HomeUiState>
    fun startStt()
    fun stopStt()
    fun fetchStuff()
}