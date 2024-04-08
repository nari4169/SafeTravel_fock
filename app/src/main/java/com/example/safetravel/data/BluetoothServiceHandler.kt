package com.example.safetravel.data

interface BluetoothServiceHandler {
    fun onReadMessage(message: String)

    fun onWriteMessage(isSuccessful: Boolean)

    fun onConnectionFailed()

    fun onConnectionLost()

    fun runWithBluetoothPermission(block: () -> Unit)
}