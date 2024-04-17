package com.example.safetravel.data.service

interface BluetoothServiceHandler {
    fun onReadMessage(macAddress: String, message: String)

    fun onWriteMessage(macAddress: String, isSuccessful: Boolean)

    fun onConnectionSuccess(macAddress: String)

    fun onConnectionFailed(macAddress: String)

    fun onConnectionLost(macAddress: String)
}