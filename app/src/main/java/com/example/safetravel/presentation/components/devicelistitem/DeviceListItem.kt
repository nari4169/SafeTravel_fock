package com.example.safetravel.presentation.components.devicelistitem

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.domain.model.Device
import com.example.safetravel.presentation.components.CustomizationScreen
import com.example.safetravel.presentation.components.dialog.DeleteDialog
import com.example.safetravel.presentation.components.dialog.VerificationAlertDialog
import com.example.safetravel.presentation.model.DeviceType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListItem(
    device: Device,
    onLockStateClicked: () -> Unit,
    onDeleteDevice: () -> Unit,
    onDeviceVerified: () -> Unit,
    onDeviceTypeChanged: (DeviceType) -> Unit,
    onRetryConnectionClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showVerifyDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()
    val verificationToastMessage = stringResource(R.string.lbl_verification_successful)

    ElevatedCard(elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)) {
        Box {
            DeviceContent(
                device = device,
                onLockStateClicked = onLockStateClicked,
                onCustomizeClick = { showBottomSheet = true },
                onDeleteClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            if (!device.isConnected || !device.isVerified || device.isConnectionLoading) {
                DeviceOverlay(
                    isConnected = device.isConnected,
                    isConnectionLoading = device.isConnectionLoading,
                    onDeleteClick = { showDeleteDialog = true },
                    onVerifyClick = { showVerifyDialog = true },
                    onRetryConnectionClick = onRetryConnectionClick,
                    modifier = Modifier
                        .matchParentSize()
                        .align(Alignment.Center)
                )
            }
        }
    }

    if (showVerifyDialog) {
        VerificationAlertDialog(
            device = device,
            onDismiss = { showVerifyDialog = false },
            onVerificationSuccessful = {
                showVerifyDialog = false
                onDeviceVerified()
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
                onDeleteDevice()
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
                            onDeviceTypeChanged(deviceType)
                        }
                    }
                )
            }
        )
    }
}
