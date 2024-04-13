package com.example.safetravel.presentation.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safetravel.domain.model.DetailedPermission
import com.example.safetravel.presentation.components.PermissionDialog
import com.example.safetravel.presentation.theme.SafeTravelTheme
import com.example.safetravel.presentation.viewmodel.PermissionsViewModel
import org.koin.androidx.compose.koinViewModel

class StartupChecksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeTravelTheme {
                PermissionsCheck()
            }
        }
    }

    @Composable
    private fun PermissionsCheck() {
        val permissionsViewModel = koinViewModel<PermissionsViewModel>()
        val uiState by permissionsViewModel.permissionsUiState.collectAsStateWithLifecycle()
        val multiplePermissionsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                uiState.permissionsToRequest.forEach { permission ->
                    val detailedPermission = when (permission) {
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH -> DetailedPermission.BluetoothPermission

                        else -> return@forEach
                    }
                    permissionsViewModel.onPermissionResult(
                        permission = detailedPermission,
                        isGranted = permissions[permission] == true
                    )
                }
                permissionsViewModel.onPermissionsChecked()
            }
        )

        if (!uiState.arePermissionsChecked) {
            SideEffect {
                multiplePermissionsLauncher.launch(uiState.permissionsToRequest.toTypedArray())
            }
        }

        if (uiState.arePermissionsChecked && uiState.visiblePermissionDialogQueue.isEmpty()) {
            MainActivity.startActivity(this@StartupChecksActivity)
            this@StartupChecksActivity.finish()
        }

        uiState.visiblePermissionDialogQueue
            .reversed()
            .forEach { detailedPermission ->
                PermissionDialog(
                    permission = detailedPermission,
                    isPermanentlyDeclined = detailedPermission.permissions.map {
                        !shouldShowRequestPermissionRationale(it)
                    }.any { it },
                    onDismiss = permissionsViewModel::dismissDialog,
                    onOkClick = {
                        permissionsViewModel.dismissDialog()
                        multiplePermissionsLauncher.launch(detailedPermission.permissions)
                    },
                    onGrantClick = ::openAppSettings,
                    modifier = Modifier
                )
            }
    }
}
