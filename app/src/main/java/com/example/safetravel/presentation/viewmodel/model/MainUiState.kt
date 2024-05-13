package com.example.safetravel.presentation.viewmodel.model

import com.example.safetravel.domain.model.BluetoothStatus
import com.example.safetravel.domain.model.Device

data class MainUiState(
    val bluetoothStatus: BluetoothStatus = BluetoothStatus.OFF,
    val devices: List<Device> = emptyList(),
    val isLoading: Boolean = true,
    val nfcSelectedDeviceAddress: String? = null,
    val isAuthenticationRequired: Boolean = true
)
