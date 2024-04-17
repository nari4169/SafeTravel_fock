package com.example.safetravel.di

import androidx.room.Room
import com.example.safetravel.data.database.SafeTravelDatabase
import com.example.safetravel.data.datasource.BluetoothStatusDataSource
import com.example.safetravel.data.repository.DeviceRepository
import com.example.safetravel.data.repository.DeviceRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = SafeTravelDatabase::class.java,
            name = SafeTravelDatabase.DATABASE_FILE
        ).build()
    }

    factory { get<SafeTravelDatabase>().deviceDao() }
    factory<DeviceRepository> { DeviceRepositoryImpl(get()) }
    single { BluetoothStatusDataSource(androidContext(), get()) }
}
