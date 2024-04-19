package com.example.safetravel.data.service

/**
 * Interface that has to be implemented by a handler which will
 * manage the connection states accordingly
 */
interface BluetoothServiceHandler {
    fun onReadMessage(macAddress: String, message: String)

    fun onWriteMessage(macAddress: String, isSuccessful: Boolean)

    fun onStartConnecting(macAddress: String)

    fun onConnectionSuccess(macAddress: String)

    fun onConnectionFailed(macAddress: String)

    fun onConnectionLost(macAddress: String)
}