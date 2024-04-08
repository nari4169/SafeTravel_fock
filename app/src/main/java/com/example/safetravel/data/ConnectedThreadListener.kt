package com.example.safetravel.data

interface ConnectedThreadListener {

    fun onReadMessage(inputBytes: Int, buffer: ByteArray)

    fun onWriteMessage(isSuccessful: Boolean)

    fun onConnectionLost()
}
