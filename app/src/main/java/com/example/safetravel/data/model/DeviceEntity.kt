package com.example.safetravel.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey @ColumnInfo(name = "macAddress") val macAddress: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "isLocked") val isLocked: Boolean = false,
    @ColumnInfo(name = "uuid") val uuid: String? = null,
)
