package com.idfm.hackathon.di

import com.idfm.hackathon.app.HackathonApp
import com.idfm.hackathon.data.repositories.sample.SampleRepository
import com.idfm.hackathon.data.repositories.sample.SampleRepositoryImpl
import com.idfm.hackathon.data.repositories.sample.SampleRetrofitClient
import com.idfm.hackathon.data.repositories.samplewebsocket.FakeWebsocketRepoImpl
import com.idfm.hackathon.data.repositories.samplewebsocket.SampleWebsocketRepoImpl
import com.idfm.hackathon.data.repositories.samplewebsocket.WebsocketRepository
import com.idfm.hackathon.ui.features.chat.ChatScreenViewModelImpl
import com.idfm.hackathon.ui.features.home.HomeScreenViewModel
import com.idfm.hackathon.ui.features.home.HomeScreenViewModelImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

@JvmField
val diModules = module {
    viewModel {
        HomeScreenViewModelImpl(androidApplication() as HackathonApp, get())
    } bind HomeScreenViewModel::class

    viewModel {
        ChatScreenViewModelImpl(androidApplication() as HackathonApp, get())
    }

    factory {
        SampleRepositoryImpl(SampleRetrofitClient.apiService)
    } bind SampleRepository::class

    factory {
//        SampleWebsocketRepoImpl()
        FakeWebsocketRepoImpl()
    } bind WebsocketRepository::class
}