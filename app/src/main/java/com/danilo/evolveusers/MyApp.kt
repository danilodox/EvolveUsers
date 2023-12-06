package com.danilo.evolveusers

import android.app.Application
import com.danilo.evolveusers.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(appModules)
        }
    }
}