package com.example.safetravel.presentation.components

import android.bluetooth.BluetoothDevice
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safetravel.R
import com.example.safetravel.data.service.BluetoothService
import com.example.safetravel.domain.model.Device
import com.example.safetravel.domain.model.DeviceMessage
import com.example.safetravel.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(
    viewModel: MainViewModel,
    bondedDevices: List<BluetoothDevice>,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var bluetoothServices by remember { mutableStateOf<List<BluetoothService>>(emptyList()) }
    var showVerifyDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deviceForVerification by remember { mutableStateOf<Device?>(null) }
    var deviceForDeletion by remember { mutableStateOf<Device?>(null) }
    var deviceForCustomization by remember { mutableStateOf<Device?>(null) }
    val verificationToastMessage = stringResource(R.string.lbl_verification_successful)
    val modalBottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

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
        modifier = Modifier.animateContentSize()
    ) {
        items(uiState.devices, { it.macAddress }) { device ->
            DeviceListItem(
                device = device,
                onDeleteClick = {
                    deviceForDeletion = device
                    showDeleteDialog = true
                },
                onVerifyClick = {
                    deviceForVerification = device
                    showVerifyDialog = true
                },
                onCustomizeClick = {
                    deviceForCustomization = device
                    showBottomSheet = true
                },
                onLockStateClicked = {
                    viewModel.changeLockedState(device.macAddress)
                    val service = bluetoothServices.first { it.device.address == device.macAddress }
                    service.write(DeviceMessage.LOCK_STATE_CHANGED.tag)
                },
                onRetryConnectionClick = {
                    val service = bluetoothServices.first { it.device.address == device.macAddress }
                    service.retryConnection()
                },
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
                    showVerifyDialog = false
                    deviceForVerification = null
                    viewModel.markDeviceAsVerified(it.macAddress)
                    Toast.makeText(context, verificationToastMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }


    if (showDeleteDialog) {
        deviceForDeletion?.let { device ->
            DeleteDialog(
                deviceName = device.name,
                onConfirm = {
                    showDeleteDialog = false
                    deviceForDeletion = null
                    bluetoothServices.firstOrNull {
                        it.device.address == device.macAddress
                    }?.apply { stop() }

                    bluetoothServices.toMutableList().removeIf {
                        it.device.address == device.macAddress
                    }

                    viewModel.deleteDevice(device.macAddress)
                },
                onDismiss = {
                    showDeleteDialog = false
                    deviceForDeletion = null
                },
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = modalBottomSheetState,
            onDismissRequest = {
                showBottomSheet = false
                deviceForCustomization = null
            },
            content = {
                CustomizationScreen(
                    onDeviceTypeClick = { deviceType ->
                        deviceForCustomization?.let { device ->
                            val job = coroutineScope.launch { modalBottomSheetState.hide() }
                            job.invokeOnCompletion {
                                showBottomSheet = false
                                deviceForCustomization = null
                                viewModel.changeDeviceType(device.macAddress, deviceType)
                            }
                        }
                    }
                )
            }
        )
    }
}
