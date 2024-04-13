package com.example.safetravel.data.datasource

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.example.safetravel.domain.isBluetoothPermissionGranted
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
    companion object {
        private const val INVALID_BLUETOOTH_STATE = -1
    }

    private val _bluetoothStatus: StateFlow<BluetoothStatus> = createFlow()
        .distinctUntilChanged()
        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            getBluetoothStatus(isBluetoothAdapterEnabled())
        )

    val bluetoothStatus = _bluetoothStatus
    private fun createFlow(): Flow<BluetoothStatus> {
        return callbackFlow {
            val broadcastReceiver = createBroadcastReceiver { isBluetoothOn ->
                trySend(getBluetoothStatus(isBluetoothOn))
            }
            val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            context.registerReceiver(broadcastReceiver, intentFilter)

            awaitClose {
                context.unregisterReceiver(broadcastReceiver)
            }
        }
    }

    private fun getBluetoothStatus(isBluetoothOn: Boolean): BluetoothStatus {
        return when {
            !context.isBluetoothPermissionGranted() -> BluetoothStatus.NOT_GRANTED
            isBluetoothOn -> BluetoothStatus.ON
            else -> BluetoothStatus.OFF
        }
    }

    private fun isBluetoothAdapterEnabled(): Boolean {
        return ContextCompat.getSystemService(context, BluetoothManager::class.java)
            ?.adapter
            ?.isEnabled
            ?: false
    }

    private fun createBroadcastReceiver(notify: (Boolean) -> Unit): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (BluetoothAdapter.ACTION_STATE_CHANGED == intent?.action) {
                    val state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        INVALID_BLUETOOTH_STATE
                    )
                    val isBluetoothOn = when (state) {
                        BluetoothAdapter.STATE_ON -> true
                        BluetoothAdapter.STATE_OFF -> false
                        else -> return
                    }
                    notify(isBluetoothOn)
                }
            }
        }
    }
}