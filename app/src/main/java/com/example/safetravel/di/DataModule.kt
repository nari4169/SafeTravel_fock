package com.example.safetravel.di

import com.example.safetravel.data.datasource.BluetoothStatusDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { BluetoothStatusDataSource(androidContext(), get()) }
}
