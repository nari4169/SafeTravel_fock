package com.example.safetravel.data.repository

import com.example.safetravel.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun addDevice(device: Device)

    suspend fun deleteDevice(macAddress: String)

    suspend fun renameDevice(macAddress: String, newName: String)

    suspend fun markDeviceAsVerified(macAddress: String)

    suspend fun changeDeviceType(macAddress: String, typeId: Int)

    suspend fun updateUuid(macAddress: String, uuid: String)

    suspend fun getDevices(): List<Device>

    suspend fun getDevice(macAddress: String): Device

    suspend fun getDevicesAsFlow(): Flow<List<Device>>

    /**
     * Removes from database any saved device that is now not
     * present in the phone's paired devices list.
     */
    suspend fun reconcileDevices(bondedDevicesAddresses: List<String>)
}
