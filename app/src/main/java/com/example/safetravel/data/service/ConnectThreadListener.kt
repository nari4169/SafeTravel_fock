package com.example.safetravel.data.service

import android.bluetooth.BluetoothSocket
import com.example.safetravel.domain.model.SocketType

interface ConnectThreadListener {
    fun onSocketCreated(socketType: SocketType)

    fun onConnected(socket: BluetoothSocket, socketType: SocketType)

    fun onConnectionFailed()

    fun runWithBluetoothPermission(block: () -> Unit)
}
