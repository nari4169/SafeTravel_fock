package com.example.safetravel.data.service

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.example.safetravel.domain.model.SocketType

interface ConnectThreadListener {
    fun onConnected(socket: BluetoothSocket, device: BluetoothDevice, socketType: SocketType)

    fun onConnectionFailed(device: BluetoothDevice)

    fun runWithBluetoothPermission(block: () -> Unit)
}
