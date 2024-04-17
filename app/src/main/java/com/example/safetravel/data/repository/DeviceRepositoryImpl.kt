package com.example.safetravel.data.repository

import com.example.safetravel.data.database.DeviceDao
import com.example.safetravel.domain.model.Device
import com.example.safetravel.domain.toDevice
import com.example.safetravel.domain.toDeviceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class DeviceRepositoryImpl(private val deviceDao: DeviceDao) : DeviceRepository {
    override fun addDevice(device: Device) = deviceDao.addDevice(device.toDeviceEntity())

    override fun deleteDevice(macAddress: String) = deviceDao.deleteDevice(macAddress)

    override fun getDevice(macAddress: String): Device {
        val deviceEntity = deviceDao.getDevice(macAddress)
        return deviceEntity.toDevice()
    }

    override fun getDevices(): Flow<List<Device>> {
        return deviceDao.getDevices().transform { entities ->
            entities.map { it.toDevice() }
        }
    }
}
