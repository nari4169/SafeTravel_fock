package com.example.safetravel.presentation.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safetravel.data.datasource.BluetoothStatusDataSource
import com.example.safetravel.data.repository.DeviceRepository
import com.example.safetravel.data.service.BluetoothServiceHandler
import com.example.safetravel.domain.model.Device
import com.example.safetravel.presentation.viewmodel.model.MainUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val deviceRepository: DeviceRepository,
    private val bluetoothDataSource: BluetoothStatusDataSource
) : ViewModel(), BluetoothServiceHandler {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            bluetoothDataSource.bluetoothStatus.collectLatest { bluetoothStatus ->
                _uiState.update {
                    it.copy(bluetoothStatus = bluetoothStatus)
                }
            }
        }

        viewModelScope.launch {
            delay(LOADING_DELAY)

            deviceRepository.getDevicesAsFlow().collect { devices ->
                _uiState.update {
                    it.copy(
                        devices = devices,
                        isLoading = false
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun addDevice(device: BluetoothDevice) {
        viewModelScope.launch {
            deviceRepository.addDevice(
                Device(
                    macAddress = device.address,
                    name = device.name
                )
            )
        }
    }

    fun reconcileDevices(bondedDevicesAddresses: List<String>) {
        viewModelScope.launch {
            deviceRepository.reconcileDevices(bondedDevicesAddresses)
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

    companion object {
        private const val LOADING_DELAY = 1000L
    }
}