package com.example.safetravel.presentation.app

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.safetravel.presentation.viewmodel.model.MainUiState

@SuppressLint("MissingPermission")
@Composable
fun SafeTravelApp(
    uiState: MainUiState,
    adapter: BluetoothAdapter
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                Text(text = uiState.bluetoothStatus.toString())

            }

//            items(adapter.bondedDevices.toList()) {
//                Text(
//                    text = it.name,
//                    modifier = Modifier.clickable {
//                        BluetoothService(adapter, mainViewModel, context).connect(
//                            it, SocketType.SECURE
//                        )
//                    }
//                )
//            }
        }
    }
}
