package com.idfm.hackathon.data.repositories.sample

import com.idfm.hackathon.data.models.SampleDto
import retrofit2.http.GET

interface SampleService {
    @GET("/data")
    suspend fun fetchData(): SampleDto
}