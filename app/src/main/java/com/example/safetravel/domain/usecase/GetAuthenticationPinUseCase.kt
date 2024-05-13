package com.example.safetravel.domain.usecase

import com.example.safetravel.data.datasource.SafeTravelDataSource

class GetAuthenticationPinUseCase(private val dataSource: SafeTravelDataSource) {
    operator fun invoke() = dataSource.authenticationPinFlow
}
