package com.example.safetravel.domain.usecase

import com.example.safetravel.data.datasource.SafeTravelDataSource

class SaveAuthenticationPinUseCase(private val dataSource: SafeTravelDataSource) {
    suspend operator fun invoke(pin: String) = dataSource.savePin(pin)
}
