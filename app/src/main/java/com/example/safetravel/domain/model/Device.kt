package com.example.safetravel.domain.model

import android.os.ParcelUuid
import com.example.safetravel.presentation.model.DeviceType

data class Device(
    val macAddress: String,
    val name: String,
    val uuid: String? = null,
    val isConnected: Boolean = false,
    val isVerified: Boolean = false,
    val isConnectionLoading: Boolean = true,
    val type: DeviceType = DeviceType.SUITCASE_REGULAR,
    val uuids: Array<ParcelUuid>
)
