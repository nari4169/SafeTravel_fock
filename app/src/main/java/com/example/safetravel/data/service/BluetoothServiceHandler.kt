package com.example.safetravel.data.service

interface BluetoothServiceHandler {
    fun onReadMessage(message: String)

    fun onWriteMessage(isSuccessful: Boolean)

    fun onConnectionSuccess()

    fun onConnectionFailed()

    fun onConnectionLost()
}