package com.example.safetravel.data.service

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import com.example.safetravel.domain.model.DeviceMessage
import com.example.safetravel.domain.model.SocketType
import com.example.safetravel.domain.runWithBluetoothPermission

class BluetoothService(
    val device: BluetoothDevice,
    private val handler: BluetoothServiceHandler,
    private val context: Context
) : ConnectThreadListener, ConnectedThreadListener {
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    init {
        connect(SocketType.SECURE)
    }

    override fun onConnected(
        socket: BluetoothSocket,
        device: BluetoothDevice,
        socketType: SocketType
    ) {
        handler.onConnectionSuccess(macAddress = device.address)
        connectThread = null
        connectedThread = ConnectedThread(
            device = device,
            socket = socket,
            socketType = socketType,
            listener = this
        ).apply { start() }

        write(DeviceMessage.CONNECTED.tag)
    }

    override fun onReadMessage(device: BluetoothDevice, inputBytes: Int, buffer: ByteArray) {
        handler.onReadMessage(
            macAddress = device.address,
            message = String(buffer, NO_OFFSET, inputBytes)
        )
    }

    override fun onWriteMessage(device: BluetoothDevice, isSuccessful: Boolean) {
        handler.onWriteMessage(device.address, isSuccessful)
    }

    override fun onConnectionFailed(device: BluetoothDevice) {
        handler.onConnectionFailed(device.address)
    }

    override fun onConnectionLost(device: BluetoothDevice) {
        handler.onConnectionLost(device.address)
    }


    override fun runWithBluetoothPermission(block: () -> Unit) {
        context.runWithBluetoothPermission(block)
    }

    private fun connect(socketType: SocketType) {
        Log.i(TAG, "Start connecting")
        connectThread = ConnectThread(device, socketType, this).apply { start() }
    }

    fun write(message: String) {
        Log.i(TAG, "Write to bluetooth device")
        connectedThread?.write(message.toByteArray())
    }

    fun stop() {
        connectThread?.let {
            it.closeSocket()
            connectThread = null
        }

        connectedThread?.let {
            connectedThread = null
        }
    }

    companion object {
        private const val TAG = "BluetoothService"
        private const val NO_OFFSET = 0
    }
}