package com.idfm.hackathon.di

import com.idfm.hackathon.app.HackathonApp
import com.idfm.hackathon.ui.features.home.HomeScreenViewModel
import com.idfm.hackathon.ui.features.home.HomeScreenViewModelImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

@JvmField
val diModules = module {
    viewModel {
        HomeScreenViewModelImpl(androidApplication() as HackathonApp)
    } bind HomeScreenViewModel::class


}