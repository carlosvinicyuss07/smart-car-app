package com.glc.smartcar

import android.app.Application
import com.glc.smartcar.di.networkModule
import com.glc.smartcar.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SmartCarApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@SmartCarApp)
            modules(networkModule, repositoryModule)
        }
    }
}