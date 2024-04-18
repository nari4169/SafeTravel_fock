package com.example.safetravel.presentation.components

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safetravel.data.service.BluetoothService
import com.example.safetravel.domain.model.Device
import com.example.safetravel.domain.model.DeviceMessage
import com.example.safetravel.presentation.viewmodel.MainViewModel

@Composable
fun DevicesScreen(
    viewModel: MainViewModel,
    bondedDevices: List<BluetoothDevice>,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var bluetoothServices by remember { mutableStateOf<List<BluetoothService>>(emptyList()) }
    var showVerifyDialog by remember { mutableStateOf(false) }
    var deviceForVerification by remember { mutableStateOf<Device?>(null) }

    LaunchedEffect(uiState.devices.size) {
        val devicesAddresses = uiState.devices.map { it.macAddress }
        val servicesDeviceAddresses = bluetoothServices.map { it.device.address }
        val newDevices = bondedDevices.filter {
            it.address in devicesAddresses
        }.filter {
            it.address !in servicesDeviceAddresses
        }

        val newServices = newDevices.map {
            BluetoothService(
                device = it,
                handler = viewModel,
                context = context
            )
        }

        bluetoothServices = bluetoothServices.toMutableList().apply { addAll(newServices) }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                bluetoothServices.forEach { it.stop() }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(uiState.devices, { it.macAddress }) { device ->
            DeviceListItem(
                device = device,
                onDeleteClick = {
                    bluetoothServices.firstOrNull {
                        it.device.address == device.macAddress
                    }?.apply { stop() }

                    bluetoothServices.toMutableList().removeIf {
                        it.device.address == device.macAddress
                    }

                    viewModel.deleteDevice(device.macAddress)
                },
                onVerifyClick = {
                    deviceForVerification = device
                    showVerifyDialog = true
                },
                onLockStateClicked = {
                    viewModel.changeLockedState(device.macAddress)
                    val service = bluetoothServices.first { it.device.address == device.macAddress }
                    service.write(DeviceMessage.LOCK_STATE_CHANGED.tag)
                },
                onRetryConnectionClick = {
                    val service = bluetoothServices.first { it.device.address == device.macAddress }
                    service.retryConnection()
                }
            )
        }
    }

    if (showVerifyDialog) {
        deviceForVerification?.let {
            VerificationAlertDialog(
                device = it,
                onDismiss = {
                    showVerifyDialog = false
                    deviceForVerification = null
                },
                onVerificationSuccessful = {
                    viewModel.markDeviceAsVerified(it.macAddress)
                }
            )
        }
    }
}
