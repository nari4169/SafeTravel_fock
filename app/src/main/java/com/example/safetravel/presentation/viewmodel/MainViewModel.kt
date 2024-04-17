package com.example.safetravel.presentation.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safetravel.data.datasource.BluetoothStatusDataSource
import com.example.safetravel.data.repository.DeviceRepository
import com.example.safetravel.data.service.BluetoothServiceHandler
import com.example.safetravel.domain.model.Device
import com.example.safetravel.domain.model.DeviceMessage
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
            deviceRepository.getDevicesAsFlow().collect { devices ->
                delay(LOADING_DELAY)
                _uiState.update {
                    it.copy(
                        devices = devices,
                        isLoading = false
                    )
                }
            }
        }
    }

    override fun onReadMessage(macAddress: String, message: String) {
        Log.d(TAG, "onReadMessage(), macAddress: $macAddress, message: $message")
        if (DeviceMessage.getByTag(message) == DeviceMessage.UUUID) {
            val lockedState = message.split(MESSAGE_SEPARATOR)[LOCKED_STATE_MESSAGE_INDEX] == TRUE
            val uuid = message.split(MESSAGE_SEPARATOR)[UUID_MESSAGE_INDEX].replace(
                oldValue = DeviceMessage.UUUID.tag,
                newValue = BLANK
            )
            viewModelScope.launch {
                deviceRepository.updateLockedState(macAddress, lockedState)
                deviceRepository.updateUuid(macAddress, uuid)
            }
        }
    }

    override fun onWriteMessage(macAddress: String, isSuccessful: Boolean) {
        Log.d(TAG, "onWriteMessage(), macAddress: $macAddress, isSuccessful: $isSuccessful")
    }

    override fun onConnectionSuccess(macAddress: String) {
        Log.d(TAG, "onConnectionSuccess(), macAddress: $macAddress")
    }

    override fun onConnectionFailed(macAddress: String) {
        Log.d(TAG, "onConnectionFailed(), macAddress: $macAddress")
    }

    override fun onConnectionLost(macAddress: String) {
        Log.d(TAG, "onConnectionLost(), macAddress: $macAddress")
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val LOADING_DELAY = 1000L
        private const val MESSAGE_SEPARATOR = ";"
        private const val UUID_MESSAGE_INDEX = 0
        private const val LOCKED_STATE_MESSAGE_INDEX = 1
        private const val BLANK = ""
        private const val TRUE = "true"
    }
}
