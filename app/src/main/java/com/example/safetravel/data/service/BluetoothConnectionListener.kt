package com.example.safetravel.data.service

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.example.safetravel.domain.model.SocketType

interface BluetoothConnectionListener {
    fun onConnected(socket: BluetoothSocket, device: BluetoothDevice, socketType: SocketType)

    fun onConnectionFailed()

    fun onConnectionLost()

    fun onSocketCreated()

    fun onServerSocketCreated()

    fun onSocketAccepted(socket: BluetoothSocket, socketType: SocketType)
}
