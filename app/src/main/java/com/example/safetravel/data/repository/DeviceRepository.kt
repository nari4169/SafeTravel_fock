package com.example.safetravel.data.repository

import com.example.safetravel.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {

    fun addDevice(device: Device)

    fun deleteDevice(macAddress: String)

    fun getDevice(macAddress: String): Device

    fun getDevices(): Flow<List<Device>>
}
