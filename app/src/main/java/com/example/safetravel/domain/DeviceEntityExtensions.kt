package com.example.safetravel.domain

import android.os.ParcelUuid
import com.example.safetravel.data.model.DeviceEntity
import com.example.safetravel.domain.model.Device
import com.example.safetravel.presentation.model.DeviceType

fun DeviceEntity.toDevice(): Device {

    val uuidString = "00001101-0000-1000-8000-00805f9b34fb"
    val parcelUuid = ParcelUuid.fromString(uuidString)
    var parcelUuids = arrayOf(parcelUuid)

    return Device(
        macAddress = this.macAddress,
        name = this.name,
        uuid = this.uuid,
        isVerified = this.isVerified,
        type = DeviceType.getById(this.typeId),
        uuids = parcelUuids
    )
}
