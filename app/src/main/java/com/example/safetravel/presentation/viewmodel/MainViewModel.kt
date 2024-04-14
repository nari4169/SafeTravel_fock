package com.example.safetravel.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safetravel.data.datasource.BluetoothStatusDataSource
import com.example.safetravel.data.service.BluetoothServiceHandler
import com.example.safetravel.domain.model.BluetoothStatus
import com.example.safetravel.presentation.viewmodel.model.MainUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(
    private val bluetoothDataSource: BluetoothStatusDataSource
) : ViewModel(), BluetoothServiceHandler {

    var uiState by mutableStateOf(MainUiState())
        private set

    init {
        viewModelScope.launch {
            bluetoothDataSource.bluetoothStatus.collectLatest { bluetoothStatus ->
                uiState = uiState.copy(bluetoothStatus = bluetoothStatus)
            }
        }
    }

    override fun onReadMessage(message: String) {
    }

    override fun onWriteMessage(isSuccessful: Boolean) {
    }

    override fun onConnectionSuccess() {
    }

    override fun onConnectionFailed() {
    }

    override fun onConnectionLost() {
    }
}