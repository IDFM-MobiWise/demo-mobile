package com.idfm.hackathon.data.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SampleRetrofitClient {
    private const val BASE_URL = "https://user-yciurlik-780729-user.data-platform-self-service.net/"

    val apiService: SampleService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SampleService::class.java)
}