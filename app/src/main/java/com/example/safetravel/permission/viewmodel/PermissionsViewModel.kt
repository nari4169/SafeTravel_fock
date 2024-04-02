package com.example.safetravel.permission.viewmodel

import androidx.lifecycle.ViewModel
import com.example.safetravel.permission.model.DetailedPermission
import com.example.safetravel.permission.model.PermissionsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionsViewModel : ViewModel() {
    private val _permissionsUiState = MutableStateFlow(PermissionsUiState())
    val permissionsUiState = _permissionsUiState.asStateFlow()

    fun dismissDialog() = _permissionsUiState.value.visiblePermissionDialogQueue.removeFirst()

    fun onPermissionResult(permission: DetailedPermission, isGranted: Boolean) {
        val queue = _permissionsUiState.value.visiblePermissionDialogQueue
        val arePermissionsChecked = _permissionsUiState.value.arePermissionsChecked
        if (!isGranted && !queue.contains(permission) && !arePermissionsChecked) {
            _permissionsUiState.value.visiblePermissionDialogQueue.add(permission)
        }
    }

    fun onPermissionsChecked() = _permissionsUiState.update {
        it.copy(arePermissionsChecked = true)
    }
}
