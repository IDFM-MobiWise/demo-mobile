package com.idfm.hackathon.data.repositories

import com.idfm.hackathon.data.models.SampleDto
import kotlinx.coroutines.flow.StateFlow

interface SampleLocalSource {
    @GET("/data")
    suspend fun getSampleData(): StateFlow<SampleDto>
}