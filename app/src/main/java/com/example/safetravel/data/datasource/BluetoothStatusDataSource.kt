package com.example.safetravel.data.datasource

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.example.safetravel.domain.model.BluetoothStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class BluetoothStatusDataSource(
    private val context: Context,
    coroutineScope: CoroutineScope
) {
    private val _bluetoothStatus: StateFlow<BluetoothStatus> = createFlow()
        .distinctUntilChanged()
        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            getBluetoothStatus()
        )

    val bluetoothStatus = _bluetoothStatus

    private fun createFlow(): Flow<BluetoothStatus> {
        return callbackFlow {
            val broadcastReceiver = createBroadcastReceiver { bluetoothStatus ->
                trySend(bluetoothStatus)
            }
            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            context.registerReceiver(broadcastReceiver, intentFilter)

            awaitClose {
                context.unregisterReceiver(broadcastReceiver)
            }
        }
    }

    private fun getBluetoothStatus(): BluetoothStatus {
        val manager = ContextCompat.getSystemService(context, BluetoothManager::class.java)
        return if (manager?.adapter?.isEnabled == true) {
            BluetoothStatus.ON
        } else {
            BluetoothStatus.OFF
        }
    }

    private fun createBroadcastReceiver(notify: (BluetoothStatus) -> Unit): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (BluetoothAdapter.ACTION_STATE_CHANGED == intent?.action) {
                    val state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        INVALID_BLUETOOTH_STATE
                    )
                    val bluetoothStatus = when (state) {
                        BluetoothAdapter.STATE_ON -> BluetoothStatus.ON
                        BluetoothAdapter.STATE_OFF -> BluetoothStatus.OFF
                        else -> return
                    }
                    notify(bluetoothStatus)
                }
            }
        }
    }

    companion object {
        private const val INVALID_BLUETOOTH_STATE = -1
    }
}