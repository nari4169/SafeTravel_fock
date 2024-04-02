package com.example.safetravel.permission.model

import android.Manifest
import android.os.Build
import com.example.safetravel.R


sealed class DetailedPermission(
    val permissions: Array<String>,
    val genericMessageId: Int,
    val declinedMessageId: Int
) {
    data object BluetoothPermission : DetailedPermission(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
            )
        },
        genericMessageId = R.string.lbl_permission_bluetooth_generic_message,
        declinedMessageId = R.string.lbl_permission_bluetooth_declined_message
    )
}
