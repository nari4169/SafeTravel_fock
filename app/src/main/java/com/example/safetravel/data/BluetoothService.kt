package com.example.safetravel.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.safetravel.domain.model.SocketType

class BluetoothService(
    private val adapter: BluetoothAdapter,
    private val handler: BluetoothServiceHandler
) : ConnectThreadListener, ConnectedThreadListener {
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    @SuppressLint("MissingPermission")
    override fun onSocketCreated() {
        Log.i(TAG, "Bluetooth adapter discovery canceled")
        handler.runWithBluetoothPermission {
            adapter.cancelDiscovery()
        }
    }

    override fun onConnected(
        socket: BluetoothSocket,
        device: BluetoothDevice,
        socketType: SocketType
    ) {
        connectThread = null
        connectedThread = ConnectedThread(socket, socketType, this).apply { start() }
    }

    override fun onReadMessage(inputBytes: Int, buffer: ByteArray) {
        handler.onReadMessage(String(buffer, NO_OFFSET, inputBytes))
    }

    override fun onWriteMessage(isSuccessful: Boolean) = handler.onWriteMessage(isSuccessful)

    override fun onConnectionFailed() = handler.onConnectionFailed()

    override fun onConnectionLost() = handler.onConnectionLost()

    override fun runWithBluetoothPermission(block: () -> Unit) {
        handler.runWithBluetoothPermission(block)
    }

    fun connect(device: BluetoothDevice, socketType: SocketType) {
        Log.i(TAG, "Start connecting")
        connectThread = ConnectThread(device, socketType, this).apply { start() }
    }

    fun write(buffer: ByteArray) {
        Log.i(TAG, "Write to bluetooth device")
        connectedThread?.write(buffer)
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