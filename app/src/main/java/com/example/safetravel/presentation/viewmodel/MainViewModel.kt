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
import com.example.safetravel.domain.model.LockStatus
import com.example.safetravel.presentation.model.DeviceType
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

    fun markDeviceAsVerified(macAddress: String) {
        viewModelScope.launch {
            val updatedDevices = _uiState.value.devices.map { device ->
                if (device.macAddress == macAddress) {
                    device.copy(isVerified = true)
                } else {
                    device
                }
            }

            _uiState.update { it.copy(devices = updatedDevices) }
            deviceRepository.markDeviceAsVerified(macAddress)
        }
    }

    fun deleteDevice(macAddress: String) {
        viewModelScope.launch {
            deviceRepository.deleteDevice(macAddress)
        }
    }

    fun renameDevice(macAddress: String, newName: String) {
        viewModelScope.launch {
            val updatedDevices = _uiState.value.devices.map { device ->
                if (device.macAddress == macAddress) {
                    device.copy(name = newName)
                } else {
                    device
                }
            }

            _uiState.update { it.copy(devices = updatedDevices) }
            deviceRepository.renameDevice(macAddress, newName)
        }
    }

    fun changeLockStatus(macAddress: String) {
        viewModelScope.launch {
            val updatedDevices = _uiState.value.devices.map { device ->
                if (device.macAddress == macAddress) {
                    val newLockStatus = when (device.lockStatus) {
                        LockStatus.UNKNOWN -> LockStatus.UNKNOWN
                        LockStatus.LOCKED -> LockStatus.LOCKED
                        LockStatus.UNLOCKED -> LockStatus.UNLOCKED
                    }
                    deviceRepository.changeLockStatus(macAddress, newLockStatus.id)
                    device.copy(lockStatus = newLockStatus)
                } else {
                    device
                }
            }

            _uiState.update { it.copy(devices = updatedDevices) }
        }
    }

    fun changeDeviceType(macAddress: String, type: DeviceType) {
        viewModelScope.launch {
            val updatedDevices = _uiState.value.devices.map { device ->
                if (device.macAddress == macAddress) {
                    device.copy(type = type)
                } else {
                    device
                }
            }

            _uiState.update { it.copy(devices = updatedDevices) }
            deviceRepository.changeDeviceType(macAddress, type.id)
        }
    }

    fun reconcileDevices(bondedDevicesAddresses: List<String>) {
        viewModelScope.launch {
            deviceRepository.reconcileDevices(bondedDevicesAddresses)
            deviceRepository.getDevicesAsFlow().collect { devices ->
                if (devices.isNotEmpty() && uiState.value.devices.isEmpty()) delay(LOADING_DELAY)
                if (_uiState.value.devices.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            devices = devices,
                            isLoading = false
                        )
                    }
                } else {
                    val devicesWithConnectivity = devices.map { device ->
                        val isConnected = _uiState.value.devices.firstOrNull {
                            it.macAddress == device.macAddress
                        }?.isConnected

                        val isConnectionLoading = _uiState.value.devices.firstOrNull {
                            it.macAddress == device.macAddress
                        }?.isConnectionLoading

                        device.copy(
                            isConnected = isConnected ?: false,
                            isConnectionLoading = isConnectionLoading ?: false
                        )
                    }

                    _uiState.update {
                        it.copy(
                            devices = devicesWithConnectivity,
                            isLoading = false
                        )
                    }
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

            val lockStatus = when (lockedState) {
                true -> LockStatus.LOCKED
                false -> LockStatus.UNLOCKED
            }

            viewModelScope.launch {
                deviceRepository.changeLockStatus(macAddress, lockStatus.id)
                deviceRepository.updateUuid(macAddress, uuid)
            }
        }
    }

    override fun onWriteMessage(macAddress: String, isSuccessful: Boolean) {
        Log.d(TAG, "onWriteMessage(), macAddress: $macAddress, isSuccessful: $isSuccessful")
    }

    override fun onStartConnecting(macAddress: String) {
        val devices = _uiState.value.devices.map { device ->
            if (device.macAddress == macAddress) {
                device.copy(isConnectionLoading = true)
            } else {
                device
            }
        }

        _uiState.update { it.copy(devices = devices) }
    }

    override fun onConnectionSuccess(macAddress: String) {
        Log.d(TAG, "onConnectionSuccess(), macAddress: $macAddress")
        val devices = _uiState.value.devices.map { device ->
            if (device.macAddress == macAddress) {
                device.copy(
                    isConnected = true,
                    isConnectionLoading = false,
                )
            } else {
                device
            }
        }

        _uiState.update { it.copy(devices = devices) }
    }

    override fun onConnectionFailed(macAddress: String) {
        Log.d(TAG, "onConnectionFailed(), macAddress: $macAddress")
        val devices = _uiState.value.devices.map { device ->
            if (device.macAddress == macAddress) {
                device.copy(
                    isConnected = false,
                    isConnectionLoading = false,
                )
            } else {
                device
            }
        }

        _uiState.update { it.copy(devices = devices) }
    }

    override fun onConnectionLost(macAddress: String) {
        Log.d(TAG, "onConnectionLost(), macAddress: $macAddress")
        val devices = _uiState.value.devices.map { device ->
            if (device.macAddress == macAddress) {
                device.copy(
                    isConnected = false,
                    isConnectionLoading = false,
                )
            } else {
                device
            }
        }

        _uiState.update { it.copy(devices = devices) }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val LOADING_DELAY = 500L
        private const val MESSAGE_SEPARATOR = ";"
        private const val UUID_MESSAGE_INDEX = 0
        private const val LOCKED_STATE_MESSAGE_INDEX = 1
        private const val BLANK = ""
        private const val TRUE = "true"
    }
}
