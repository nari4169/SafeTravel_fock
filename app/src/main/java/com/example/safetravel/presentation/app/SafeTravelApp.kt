package com.example.safetravel.presentation.app

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safetravel.R
import com.example.safetravel.domain.model.BluetoothStatus
import com.example.safetravel.presentation.components.AddDeviceScreen
import com.example.safetravel.presentation.components.DevicesScreen
import com.example.safetravel.presentation.components.EmptyScreen
import com.example.safetravel.presentation.components.LoadingScreen
import com.example.safetravel.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun SafeTravelApp(
    viewModel: MainViewModel,
    bondedDevices: List<BluetoothDevice>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val bluetoothOffToastMessage = stringResource(R.string.lbl_bluetooth_off_toast)
    val modalBottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (showBottomSheet && uiState.bluetoothStatus == BluetoothStatus.OFF) {
        showBottomSheet = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingScreen()
            uiState.devices.isEmpty() -> EmptyScreen()
            else -> DevicesScreen(
                devices = uiState.devices,
                handler = viewModel,
                bondedDevices = bondedDevices,
                onDeviceLockedStateChanged = viewModel::changeLockStatus,
                onDeleteDevice = viewModel::deleteDevice,
                onDeviceVerified = viewModel::markDeviceAsVerified,
                onRenameDevice = viewModel::renameDevice,
                onDeviceTypeChanged = viewModel::changeDeviceType
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = modalBottomSheetState,
                onDismissRequest = { showBottomSheet = false },
                content = {
                    AddDeviceScreen(
                        bondedDevices = bondedDevices.filter { bondedDevice ->
                            bondedDevice.address !in uiState.devices.map { it.macAddress }
                        },
                        onDeviceClick = { bluetoothDevice ->
                            val job = coroutineScope.launch { modalBottomSheetState.hide() }
                            job.invokeOnCompletion {
                                showBottomSheet = false
                                viewModel.addDevice(bluetoothDevice)
                            }
                        }
                    )
                }
            )
        }

        ExtendedFloatingActionButton(
            text = {
                Text(text = stringResource(R.string.lbl_add_device))
            },
            icon = {
                Icon(
                    modifier = Modifier.size(56.dp),
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null
                )
            },
            onClick = {
                when (uiState.bluetoothStatus) {
                    BluetoothStatus.ON -> showBottomSheet = true
                    BluetoothStatus.OFF -> {
                        Toast.makeText(context, bluetoothOffToastMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}
