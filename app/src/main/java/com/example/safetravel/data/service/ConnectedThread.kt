package com.example.safetravel.data.service

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.safetravel.domain.model.SocketType
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectedThread(
    socket: BluetoothSocket,
    private val socketType: SocketType,
    private val listener: ConnectedThreadListener
) : Thread() {

    private val inputStream: InputStream = socket.inputStream
    private val outputStream: OutputStream = socket.outputStream

    override fun run() {
        val buffer = ByteArray(BUFFER_SIZE)

        while (true) {
            if (inputStream.available() > NO_INPUT_STREAM_BYTES) {
                runBlocking { delay(DELAY_TIME) }
                try {
                    val inputBytes = inputStream.read(buffer)
                    listener.onReadMessage(inputBytes, buffer)
                } catch (readException: IOException) {
                    Log.e(TAG, "Failed to read from socket: $socketType", readException)
                    listener.onConnectionLost()
                    break
                }
            }
        }
    }

    fun write(buffer: ByteArray) {
        try {
            outputStream.write(buffer)
            listener.onWriteMessage(true)
            Log.i(TAG, "Successful write on Socket: $socketType")
        } catch (writeException: IOException) {
            listener.onWriteMessage(false)
            Log.e(TAG, "Failed to write to socket: $socketType", writeException)
        }
    }

    companion object {
        private const val TAG = "ConnectedThread"
        private const val BUFFER_SIZE = 256
        private const val NO_INPUT_STREAM_BYTES = 0
        private const val DELAY_TIME = 300L
    }
}