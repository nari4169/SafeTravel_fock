package com.example.safetravel.presentation.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safetravel.data.datasource.BluetoothStatusDataSource
import com.example.safetravel.data.repository.DeviceRepository
import com.example.safetravel.data.service.BluetoothServiceHandler
import com.example.safetravel.domain.model.Device
import com.example.safetravel.domain.model.DeviceMessage
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

    var bluetoothDevices = mutableStateListOf<BluetoothDevice>()

    val recvMsgs = mutableStateListOf<String>()
    val isScaning = mutableStateOf(false)

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
        recvMsgs.clear()
        viewModelScope.launch {
            Log.e("", "........................... addDevice ${device.name}")
            deviceRepository.addDevice(
                Device(
                    macAddress = device.address,
                    name = device.name,
                    uuids = device.uuids ?: emptyArray(),
                    isConnectionLoading = false,
                    isConnected = true
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

    fun selectNfcDevice(macAddress: String) {
        _uiState.update {
            it.copy(nfcSelectedDeviceAddress = macAddress)
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
        Log.e(TAG, "onReadMessage(), macAddress: $macAddress, message: $message")
        recvMsgs.add(String.format("onReadMessage=%s, %s, %s", message, DeviceMessage.getByTag(message), DeviceMessage.UUID))
        if (DeviceMessage.getByTag(message) == DeviceMessage.UUID) {
            val uuid = message.split(MESSAGE_SEPARATOR)[UUID_MESSAGE_INDEX].replace(
                oldValue = DeviceMessage.UUID.tag,
                newValue = BLANK
            )
            viewModelScope.launch {
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
        private const val UUID_MESSAGE_INDEX = 1
        private const val BLANK = ""
    }
}
