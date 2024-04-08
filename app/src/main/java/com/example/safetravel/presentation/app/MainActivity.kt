package com.example.safetravel.presentation.app

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.safetravel.data.BluetoothServiceHandler
import com.example.safetravel.domain.runWithBluetoothPermission
import com.example.safetravel.presentation.theme.SafeTravelTheme

class MainActivity : ComponentActivity(), BluetoothServiceHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeTravelTheme {
                val manager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                SafeTravelApp()
            }
        }
    }

    override fun onReadMessage(message: String) {
        Log.i(TAG, "Read message: $message")
    }

    override fun onWriteMessage(isSuccessful: Boolean) {
        Log.i(TAG, "Write message was successful: $isSuccessful")
    }

    override fun onConnectionFailed() {
        Log.i(TAG, "Connection failed")
    }

    override fun onConnectionLost() {
        Log.i(TAG, "Connection lost")
    }

    override fun runWithBluetoothPermission(block: () -> Unit) {
        (this as Context).runWithBluetoothPermission(block)
    }

    companion object {
        private const val TAG = "MainActivity"
        fun startActivity(activity: StartupChecksActivity) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }

            activity.startActivity(intent)
            activity.finish()
        }
    }
}
