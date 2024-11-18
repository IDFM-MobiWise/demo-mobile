package com.idfm.hackathon.app

import android.app.Application
import com.idfm.hackathon.di.diModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class HackathonApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HackathonApp)
            modules(diModules)
        }
    }
}