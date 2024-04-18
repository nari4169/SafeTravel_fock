package com.example.safetravel.data.service

interface ConnectedThreadListener {

    fun onReadMessage(inputBytes: Int, buffer: ByteArray)

    fun onWriteMessage(isSuccessful: Boolean)

    fun onConnectionLost()
}
