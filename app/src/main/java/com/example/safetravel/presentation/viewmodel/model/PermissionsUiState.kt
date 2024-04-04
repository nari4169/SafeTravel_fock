package com.example.safetravel.presentation.viewmodel.model

import android.Manifest
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.safetravel.domain.model.DetailedPermission

data class PermissionsUiState(
    val visiblePermissionDialogQueue: SnapshotStateList<DetailedPermission> = mutableStateListOf(),
    val arePermissionsChecked: Boolean = false,
    val permissionsToRequest: List<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(Manifest.permission.BLUETOOTH_CONNECT)
    } else {
        listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
        )
    }
)
