package com.example.safetravel.domain.model

import android.os.ParcelUuid
import com.example.safetravel.presentation.model.DeviceType

data class Device(
    val macAddress: String,
    val name: String,
    val uuid: String? = null,
    var isConnected: Boolean = false,
    val isVerified: Boolean = false,
    var isConnectionLoading: Boolean = true,
    val type: DeviceType = DeviceType.SUITCASE_REGULAR,
    val uuids: Array<ParcelUuid>
)
