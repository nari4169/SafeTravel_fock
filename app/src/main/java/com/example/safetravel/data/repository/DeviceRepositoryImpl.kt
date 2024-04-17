package com.example.safetravel.data.repository

import com.example.safetravel.data.database.DeviceDao
import com.example.safetravel.domain.model.Device
import com.example.safetravel.domain.toDevice
import com.example.safetravel.domain.toDeviceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DeviceRepositoryImpl(private val deviceDao: DeviceDao) : DeviceRepository {
    override suspend fun addDevice(device: Device) {
        withContext(Dispatchers.Default) {
            deviceDao.addDevice(device.toDeviceEntity())
        }
    }

    override suspend fun deleteDevice(macAddress: String) {
        withContext(Dispatchers.Default) {
            deviceDao.deleteDevice(macAddress)
        }
    }

    override suspend fun updateLockedState(macAddress: String, isLocked: Boolean) {
        withContext(Dispatchers.Default) {
            val device = deviceDao.getDevice(macAddress)
            deviceDao.updateDevice(
                device.copy(isLocked = isLocked)
            )
        }
    }

    override suspend fun updateUuid(macAddress: String, uuid: String) {
        withContext(Dispatchers.Default) {
            val device = deviceDao.getDevice(macAddress)
            deviceDao.updateDevice(
                device.copy(uuid = uuid)
            )
        }
    }

    override suspend fun getDevices(): List<Device> {
        return withContext(Dispatchers.Default) {
            return@withContext deviceDao.getDevices().map { it.toDevice() }
        }
    }


    override suspend fun getDevice(macAddress: String): Device {
        return withContext(Dispatchers.Default) {
            val deviceEntity = deviceDao.getDevice(macAddress)
            return@withContext deviceEntity.toDevice()
        }
    }

    override suspend fun getDevicesAsFlow(): Flow<List<Device>> {
        return withContext(Dispatchers.Default) {
            return@withContext deviceDao.getDevicesAsFlow().map { entities ->
                entities.map { it.toDevice() }
            }
        }
    }

    override suspend fun reconcileDevices(bondedDevicesAddresses: List<String>) {
        withContext(Dispatchers.Default) {
            val devicesAddresses = deviceDao.getDevices().map { it.macAddress }
            val notBondedDevices = devicesAddresses.subtract(bondedDevicesAddresses.toSet())
            notBondedDevices.forEach { deviceDao.deleteDevice(it) }
        }
    }
}
