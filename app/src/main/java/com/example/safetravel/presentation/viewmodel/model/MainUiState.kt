package com.example.safetravel.presentation.viewmodel.model

import com.example.safetravel.domain.model.BluetoothStatus

data class MainUiState(
    val bluetoothStatus: BluetoothStatus = BluetoothStatus.OFF,
)
