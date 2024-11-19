package com.idfm.hackathon.data.repositories

import com.idfm.hackathon.data.models.Sample
import kotlinx.coroutines.flow.StateFlow


sealed class SampleRepositoryState {
    data class Success(val result: Sample) : SampleRepositoryState()
    data object Error : SampleRepositoryState()
    data object Loading : SampleRepositoryState()
}

interface SampleRepository {
    fun state(): StateFlow<SampleRepositoryState>
    fun get()
}