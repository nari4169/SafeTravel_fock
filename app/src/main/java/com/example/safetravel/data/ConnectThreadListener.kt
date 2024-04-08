package com.example.safetravel.data

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.example.safetravel.domain.model.SocketType

interface ConnectThreadListener {
    fun onSocketCreated()

    fun onConnected(socket: BluetoothSocket, device: BluetoothDevice, socketType: SocketType)

    fun onConnectionFailed()

    fun runWithBluetoothPermission(block: () -> Unit)
}
