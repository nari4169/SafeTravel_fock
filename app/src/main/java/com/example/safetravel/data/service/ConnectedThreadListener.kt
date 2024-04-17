package com.example.safetravel.data.service

import android.bluetooth.BluetoothDevice

interface ConnectedThreadListener {

    fun onReadMessage(device: BluetoothDevice, inputBytes: Int, buffer: ByteArray)

    fun onWriteMessage(device: BluetoothDevice, isSuccessful: Boolean)

    fun onConnectionLost(device: BluetoothDevice)
}
