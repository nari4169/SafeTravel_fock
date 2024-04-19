package com.example.safetravel.presentation.components.devicelistitem

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.domain.model.Device
import com.example.safetravel.domain.model.LockStatus
import com.example.safetravel.presentation.components.CustomizationScreen
import com.example.safetravel.presentation.components.dialog.DeleteDialog
import com.example.safetravel.presentation.components.dialog.RenameDialog
import com.example.safetravel.presentation.components.dialog.VerificationDialog
import com.example.safetravel.presentation.model.DeviceType
import com.example.safetravel.presentation.theme.SafeTravelTheme
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListItem(
    device: Device,
    onLockStateChanged: () -> Unit,
    onNfcClicked: () -> Unit,
    onDelete: () -> Unit,
    onVerified: () -> Unit,
    onRename: (String) -> Unit,
    onTypeChanged: (DeviceType) -> Unit,
    onRetryConnection: () -> Unit
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
                    onLockStateClicked = {
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

                            else -> onLockStateChanged()
                        }
                    },
                    onNfcClicked = onNfcClicked,
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
    SafeTravelTheme {
        DeviceListItem(
            device = Device(
                macAddress = UUID.randomUUID().toString(),
                name = "Backpack",
                lockStatus = LockStatus.LOCKED,
                uuid = UUID.randomUUID().toString(),
                isConnected = false,
                isVerified = false,
                isConnectionLoading = false,
                type = DeviceType.BACKPACK
            ),
            onLockStateChanged = {},
            onNfcClicked = {},
            onDelete = {},
            onVerified = {},
            onRename = {},
            onTypeChanged = {},
            onRetryConnection = {},
        )
    }
}
