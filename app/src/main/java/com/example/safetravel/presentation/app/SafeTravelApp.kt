package com.example.safetravel.presentation.app

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.safetravel.presentation.components.PermissionNotGrantedScreen
import com.example.safetravel.presentation.viewmodel.model.MainUiState

@SuppressLint("MissingPermission")
@Composable
fun SafeTravelApp(
    uiState: MainUiState,
    adapter: BluetoothAdapter,
    modifier: Modifier = Modifier,
) {
}
