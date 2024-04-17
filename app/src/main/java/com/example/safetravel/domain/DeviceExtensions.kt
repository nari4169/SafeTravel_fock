package com.example.safetravel.domain

import com.example.safetravel.data.model.DeviceEntity
import com.example.safetravel.domain.model.Device

fun Device.toDeviceEntity(): DeviceEntity {
    return DeviceEntity(
        macAddress = this.macAddress,
        name = this.name,
        isLocked = this.isLocked,
        uuid = this.uuid
    )
}
