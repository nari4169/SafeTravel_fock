package com.example.safetravel.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.safetravel.data.model.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDevice(deviceEntity: DeviceEntity)

    @Query("DELETE FROM devices WHERE macAddress LIKE :macAddress")
    fun deleteDevice(macAddress: String)

    @Query("SELECT * FROM devices")
    fun getDevices(): List<DeviceEntity>

    @Query("SELECT * FROM devices WHERE macAddress LIKE :macAddress")
    fun getDevice(macAddress: String): DeviceEntity

    @Query("SELECT * FROM devices")
    fun getDevicesAsFlow(): Flow<List<DeviceEntity>>
}
