package com.example.safetravel.data.service

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import com.example.safetravel.domain.model.DeviceMessage
import com.example.safetravel.domain.model.SocketType
import com.example.safetravel.domain.runWithBluetoothPermission

/**
 * Service for managing thread for bluetooth connections on threads
 */
class BluetoothService(
    val device: BluetoothDevice,
    private val handler: BluetoothServiceHandler,
    private val context: Context
) : ConnectThreadListener, ConnectedThreadListener {
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    init {
        Log.e(TAG, "Start connection to device: ${device.address}")
        handler.onStartConnecting(device.address)
        connect(SocketType.SECURE)
    }

    @SuppressLint("MissingPermission")
    override fun onSocketCreated(socketType: SocketType) {
        Log.e(TAG, "Socket: $socketType created for device: ${device.address}")
        val service = context.getSystemService(Context.BLUETOOTH_SERVICE)
        val adapter = (service as BluetoothManager).adapter
        context.runWithBluetoothPermission { adapter.cancelDiscovery() }
    }

    override fun onConnected(socket: BluetoothSocket, socketType: SocketType) {
        Log.e(TAG, "Connected to device: ${device.address}")
        handler.onConnectionSuccess(macAddress = device.address)
        if (socket.isConnected) {
            connectedThread = ConnectedThread(
                socket = socket,
                socketType = socketType,
                listener = this
            ).apply { start() }
            connectedThread?.write(DeviceMessage.CONNECTED.tag.toByteArray())
        }
    }

    override fun onReadMessage(inputBytes: Int, buffer: ByteArray) {
        val message = String(buffer, NO_OFFSET, inputBytes)
        Log.e(TAG, "Read message from device: ${device.address}, message: $message")
        handler.onReadMessage(
            macAddress = device.address,
            message = message
        )
    }

    override fun onWriteMessage(isSuccessful: Boolean) {
        Log.e(TAG, "Write message to device: ${device.address}, successful: $isSuccessful")
        handler.onWriteMessage(device.address, isSuccessful)
    }

    override fun onConnectionFailed() {
        Log.e(TAG, "Connection failed for device: ${device.address}")
        handler.onConnectionFailed(device.address)
    }

    override fun onConnectionLost() {
        Log.e(TAG, "Connection lost for device: ${device.address}")
        handler.onConnectionLost(device.address)
    }


    override fun runWithBluetoothPermission(block: () -> Unit) {
        context.runWithBluetoothPermission(block)
    }

    @SuppressLint("MissingPermission")
    private fun connect(socketType: SocketType) {
        Log.e(TAG, "Start connecting to device: ${device.name}")
        connectThread = ConnectThread(device, socketType, this).apply { start() }
    }

    @SuppressLint("MissingPermission")
    fun retryConnection() {
        Log.e(TAG, "Retry connection for device: ${device.name}")
        handler.onStartConnecting(device.address)
        connect(SocketType.SECURE)
    }

    fun write(message: String, uuid: String) {
        Log.e(TAG, "Write to bluetooth device: ${device.address}")
        val messageWithUuid = "$message;$uuid"
        connectedThread?.write(messageWithUuid.toByteArray())
    }

    fun stop() {
        Log.e(TAG, "Stop all processes for device: ${device.address}")
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