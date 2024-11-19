package com.idfm.hackathon.data.repositories

import com.idfm.hackathon.data.models.Sample
import com.idfm.hackathon.data.models.SampleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


//sealed class SampleRepositoryState {
//    data class Success(val result: Sample) : SampleRepositoryState()
//    data object Error : SampleRepositoryState()
//    data object Loading : SampleRepositoryState()
//}

fun interface SampleRepository {
    fun fetchData(): Flow<Result<SampleDto>>
}