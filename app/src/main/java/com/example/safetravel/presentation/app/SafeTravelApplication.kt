package com.example.safetravel.presentation.app

import android.app.Application
import com.example.safetravel.di.libraryModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SafeTravelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SafeTravelApplication)
            modules(libraryModules)
        }
    }
}
