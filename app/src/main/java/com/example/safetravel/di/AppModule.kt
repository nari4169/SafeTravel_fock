package com.example.safetravel.di

import com.example.safetravel.domain.usecase.GetAuthenticationPinUseCase
import com.example.safetravel.domain.usecase.SaveAuthenticationPinUseCase
import com.example.safetravel.presentation.viewmodel.AuthenticationViewModel
import com.example.safetravel.presentation.viewmodel.MainViewModel
import com.example.safetravel.presentation.viewmodel.PermissionsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    factory { CoroutineScope(Dispatchers.IO) }
    viewModelOf(::PermissionsViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::AuthenticationViewModel)
    factoryOf(::GetAuthenticationPinUseCase)
    factoryOf(::SaveAuthenticationPinUseCase)
}
