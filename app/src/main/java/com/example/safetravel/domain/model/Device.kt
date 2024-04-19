package com.example.safetravel.domain.model

import com.example.safetravel.presentation.model.DeviceType

data class Device(
    val macAddress: String,
    val name: String,
    val lockStatus: LockStatus = LockStatus.UNKNOWN,
    val uuid: String? = null,
    val isConnected: Boolean = false,
    val isVerified: Boolean = false,
    val isConnectionLoading: Boolean = true,
    val type: DeviceType = DeviceType.SUITCASE_REGULAR
)
