package com.example.safetravel.domain.model

data class Device(
    val macAddress: String,
    val name: String,
    val isLocked: Boolean = false,
    val uuid: String? = null,
    val isConnected: Boolean = false,
    val isVerified: Boolean = false,
    val isConnectionLoading: Boolean = true,
)
