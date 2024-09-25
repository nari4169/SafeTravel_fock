package com.example.safetravel.presentation.components.devicelistitem

import android.content.res.Configuration
import android.os.ParcelUuid
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safetravel.R
import com.example.safetravel.domain.model.Device
import com.example.safetravel.presentation.components.CustomizationScreen
import com.example.safetravel.presentation.components.dialog.DeleteDialog
import com.example.safetravel.presentation.components.dialog.RenameDialog
import com.example.safetravel.presentation.components.dialog.VerificationDialog
import com.example.safetravel.presentation.model.DeviceType
import com.example.safetravel.presentation.theme.SafeTravelTheme
import com.example.safetravel.presentation.theme.softBlue
import com.example.safetravel.presentation.theme.softGray
import com.example.safetravel.presentation.theme.softWhite
import com.example.safetravel.presentation.theme.typography
import com.example.safetravel.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DeviceListItem(
    device: Device,
    viewModel: MainViewModel,
    unUnlockClick: () -> Unit,
    onNfcClick: () -> Unit,
    onDelete: () -> Unit,
    onVerified: () -> Unit,
    onRename: (String) -> Unit,
    onTypeChanged: (DeviceType) -> Unit,
    onRetryConnection: () -> Unit,
    doConnectDevice: (device: Device) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showVerifyDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()
    val verificationToastMessage = stringResource(R.string.lbl_verification_successful_toast)
    val deviceNotVerifiedToastMessage = stringResource(R.string.lbl_device_not_verified_toast)
    val deviceNotConnectedToastMessage = stringResource(R.string.lbl_device_not_connected_toast)
    val deviceUnlockedToastMessage = stringResource(R.string.lbl_device_unlocked_toast, device.name)
    var isLocked by remember { mutableStateOf(true) }
    LaunchedEffect(isLocked) {
        if (!isLocked) {
            delay(1000)
            isLocked = true
        }
    }

    val uuids = remember { mutableStateListOf("") }
    val selected = remember { mutableStateOf(false) }
    uuids.clear()
    device.uuids?.map {
        if (uuids.indexOf(it.toString()) < 0) {
            uuids.add(it.toString())
        }
    }

    ElevatedCard(elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)) {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .animateContentSize()
            ) {
                DeviceContent(
                    device = device,
                    isLocked = isLocked,
                    onUnlockClick = {
                        when {
                            !device.isConnected -> Toast.makeText(
                                context,
                                deviceNotConnectedToastMessage,
                                Toast.LENGTH_SHORT
                            ).show()

                            !device.isVerified -> Toast.makeText(
                                context,
                                deviceNotVerifiedToastMessage,
                                Toast.LENGTH_SHORT
                            ).show()

                            else -> {
                                unUnlockClick()
                                isLocked = false
                                Toast.makeText(
                                    context,
                                    deviceUnlockedToastMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onNfcClick = onNfcClick,
                    onCustomizeClick = { showBottomSheet = true },
                    onDeleteClick = { showDeleteDialog = true },
                    onRenameClick = { showRenameDialog = true },
                    modifier = Modifier.fillMaxWidth()
                )

                if (!device.isConnected || !device.isVerified) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))

                    DeviceWarningsRow(
                        verifyEnabled = !device.isVerified,
                        reconnectEnabled = !device.isConnected,
                        reconnectLoading = device.isConnectionLoading,
                        onVerifyClick = { showVerifyDialog = true },
                        onReconnectClick = onRetryConnection,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                FlowRow(
                    modifier = Modifier
                        .padding(3.dp)
                        .border(1.dp, softGray),
                ) {
                    viewModel.recvMsgs.forEach {
                        FilterChip(
                            selected.value,
                            onClick = {

                            },
                            label = {
                                Text(text = it, style = typography.bodyMedium.copy(softWhite))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.RadioButtonUnchecked,
                                    contentDescription = it,
                                    tint = softGray
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(containerColor = softBlue),
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))

                FlowRow(
                    modifier = Modifier
                        .padding(3.dp)
                        .border(1.dp, softGray),
                ) {
                    uuids.forEach {
                        FilterChip(
                            selected.value,
                            onClick = {
                                doConnectDevice(device)
                            },
                            label = {
                                Text(text = it, style = typography.bodyMedium.copy(softWhite))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.RadioButtonUnchecked,
                                    contentDescription = it,
                                    tint = softGray
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(containerColor = softBlue),
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                }

            }
        }
    }

    if (showVerifyDialog) {
        VerificationDialog(
            device = device,
            onDismiss = { showVerifyDialog = false },
            onVerificationSuccessful = {
                showVerifyDialog = false
                onVerified()
                Toast.makeText(context, verificationToastMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }


    if (showDeleteDialog) {
        DeleteDialog(
            deviceName = device.name,
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = modalBottomSheetState,
            onDismissRequest = { showBottomSheet = false },
            content = {
                CustomizationScreen(
                    onDeviceTypeClick = { deviceType ->
                        val job = coroutineScope.launch { modalBottomSheetState.hide() }
                        job.invokeOnCompletion {
                            showBottomSheet = false
                            onTypeChanged(deviceType)
                        }
                    }
                )
            }
        )
    }

    if (showRenameDialog) {
        RenameDialog(
            deviceName = device.name,
            onDismiss = { showRenameDialog = false },
            onRename = {
                showRenameDialog = false
                onRename(it)
            }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DeviceListItemPreview() {
    val uuidString = "00001101-0000-1000-8000-00805f9b34fb"
    val parcelUuid = ParcelUuid.fromString(uuidString)
    var parcelUuids = arrayOf(parcelUuid)

    SafeTravelTheme {
        DeviceListItem(
            device = Device(
                macAddress = UUID.randomUUID().toString(),
                name = "Backpack",
                uuid = UUID.randomUUID().toString(),
                isConnected = false,
                isVerified = false,
                isConnectionLoading = false,
                type = DeviceType.BACKPACK,
                uuids = parcelUuids
            ),
            viewModel = viewModel(),
            unUnlockClick = {},
            onNfcClick = {},
            onDelete = {},
            onVerified = {},
            onRename = {},
            onTypeChanged = {},
            onRetryConnection = {},
            doConnectDevice = {}
        )
    }
}
