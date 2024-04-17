package com.example.safetravel.data.repository

import com.example.safetravel.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {

    suspend fun addDevice(device: Device)

    suspend fun deleteDevice(macAddress: String)

    suspend fun changeLockedState(macAddress: String)

    suspend fun updateLockedState(macAddress: String, isLocked: Boolean)

    suspend fun updateUuid(macAddress: String, uuid: String)

    suspend fun getDevices(): List<Device>

    suspend fun getDevice(macAddress: String): Device

    suspend fun getDevicesAsFlow(): Flow<List<Device>>

    suspend fun reconcileDevices(bondedDevicesAddresses: List<String>)
}
