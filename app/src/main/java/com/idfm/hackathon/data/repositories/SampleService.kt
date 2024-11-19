package com.idfm.hackathon.data.repositories

import com.idfm.hackathon.data.models.SampleDto
import kotlinx.coroutines.flow.StateFlow
import retrofit2.http.GET

interface SampleService {
    @GET("/data")
    suspend fun fetchData(): SampleDto
}