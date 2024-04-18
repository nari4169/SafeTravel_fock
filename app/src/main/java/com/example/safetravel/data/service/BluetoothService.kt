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
        Log.i(TAG, "Start connection to device: ${device.address}")
        handler.onStartConnecting(device.address)
        connect(SocketType.SECURE)
    }

    override fun onConnected(
        socket: BluetoothSocket,
        device: BluetoothDevice,
        socketType: SocketType
    ) {
        Log.i(TAG, "Connected to device: ${device.address}")
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
        val message = String(buffer, NO_OFFSET, inputBytes)
        Log.i(TAG, "Read message from device: ${device.address}, message: $message")
        handler.onReadMessage(
            macAddress = device.address,
            message = message
        )
    }

    override fun onWriteMessage(device: BluetoothDevice, isSuccessful: Boolean) {
        Log.i(TAG, "Write message to device: ${device.address}, successful: $isSuccessful")
        handler.onWriteMessage(device.address, isSuccessful)
    }

    override fun onConnectionFailed(device: BluetoothDevice) {
        Log.i(TAG, "Connection failed for device: ${device.address}")
        handler.onConnectionFailed(device.address)
    }

    override fun onConnectionLost(device: BluetoothDevice) {
        Log.i(TAG, "Connection lost for device: ${device.address}")
        handler.onConnectionLost(device.address)
    }


    override fun runWithBluetoothPermission(block: () -> Unit) {
        context.runWithBluetoothPermission(block)
    }

    private fun connect(socketType: SocketType) {
        Log.i(TAG, "Start connecting to device: ${device.address}")
        connectThread = ConnectThread(device, socketType, this).apply { start() }
    }

    fun retryConnection() {
        Log.i(TAG, "Retry connection for device: ${device.address}")
        handler.onStartConnecting(device.address)
        connect(SocketType.SECURE)
    }

    fun write(message: String) {
        Log.i(TAG, "Write to bluetooth device: ${device.address}")
        connectedThread?.write(message.toByteArray())
    }

    fun stop() {
        Log.i(TAG, "Stop all processes for device: ${device.address}")
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