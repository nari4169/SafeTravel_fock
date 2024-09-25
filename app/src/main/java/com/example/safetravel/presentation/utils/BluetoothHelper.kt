package com.example.safetravel.presentation.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.safetravel.R
import com.example.safetravel.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Delay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay

import java.util.UUID

class BluetoothHelper(private val context: Context, dataViewModel: MainViewModel) {

    private val bluetoothAdapter: BluetoothAdapter? =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private val bluetoothScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    val viewModel = dataViewModel
    private var gatt: BluetoothGatt? = null
    lateinit var uuidString: String
    lateinit var charUuid: String

    // 스캔 결과 처리
    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission", "RestrictedApi")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val deviceName = device.name
            val deviceAddress = device.address
            val scanRecord = result.scanRecord

            viewModel.addDevice(device)
            viewModel.bluetoothDevices.add(device)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Log.e(
                    "",
                    "device ${device.name} ${device.address} ${device.alias} ${device.bluetoothClass} ${device.uuids}"
                )
            } else {
                Log.e(
                    "",
                    "device ${device.name} ${device.address} ${device.bluetoothClass} ${device.uuids}"
                )
            }

            // 원하는 UUID를 찾는 조건 추가
//            val serviceUuid = stringToParcelUuid(uuidString) // 예시 UUID
//            if (scanRecord != null && scanRecord.serviceUuids?.contains(serviceUuid) == true) {
//                Log.e("BluetoothHelper", "찾은 장치: $deviceName, $deviceAddress")
//                connectToDevice(device)
//            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BluetoothHelper", "스캔 실패: $errorCode")
        }
    }

    private fun stringToParcelUuid(uuidString: String): ParcelUuid {
        val uuid = UUID.fromString(uuidString)
        return ParcelUuid(uuid)
    }

    // 장치 연결
    @SuppressLint("MissingPermission")
    private fun connectToDevice(
        device: BluetoothDevice,
    ) {
        gatt = device.connectGatt(context, true, gattCallback)
    }

    // GATT 연결 콜백
    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.e("", "onConnectionStateChange ${newState}")
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.e("BluetoothHelper", "장치에 연결됨: ${gatt.device.name}")
                gatt.discoverServices()
            } else {
                Log.e("BluetoothHelper", "장치 연결 해제됨")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.e("", "onServicesDiscovered ${status}")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("BluetoothHelper", "서비스 발견됨 $uuidString")
                // 원하는 UUID를 사용하여 서비스 및 특성 검색
                val service = gatt.getService(UUID.fromString(uuidString)) // 예시 UUID
                Log.e("", "service ${service}")
                if (service != null) {
                    val characteristic =
                        service.getCharacteristic(UUID.fromString(charUuid)) // 예시 UUID
                    if (characteristic != null) {
                        // 특성 읽기
                        readCharacteristic(characteristic)
                    }
                }
            } else {
                Log.e("BluetoothHelper", "서비스 발견 실패: $status")
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            Log.e("", "onCharacteristicRead $status")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val data = characteristic
                // 데이터 처리 (예: 문자열 변환, 값 해석)
                Log.e("BluetoothHelper", "데이터 읽기 성공: ${data.value}")
            } else {
                Log.e("BluetoothHelper", "데이터 읽기 실패: $status")
            }
        }
    }

    // 특성 읽기
    @SuppressLint("MissingPermission")
    private fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        gatt?.readCharacteristic(characteristic)
    }

    // 스캔 시작
    @SuppressLint("MissingPermission")
    fun startScan() {
        val serviceUuid = ParcelUuid.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(serviceUuid)
            .build()
        val scanFilters = listOf(scanFilter)

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()

        //viewModel.bluetoothDevices.clear()
        if (!viewModel.isScaning.value) {
            bluetoothScanner?.startScan(scanFilters, scanSettings, scanCallback)
            viewModel.isScaning.value = true

            Handler(Looper.getMainLooper()).postDelayed({
                stopScan()
            }, 30000L)
        }
    }

    // 스캔 중지
    @SuppressLint("MissingPermission")
    fun stopScan() {
        Toast.makeText(context, context.getText(R.string.lbl_stop_scanning), Toast.LENGTH_SHORT).show()
        bluetoothScanner?.stopScan(scanCallback)
        viewModel.isScaning.value = false
    }

    // 연결 해제
    @SuppressLint("MissingPermission")
    fun disconnect() {
        gatt?.disconnect()
    }

    // 장치 닫기
    @SuppressLint("MissingPermission")
    fun close() {
        gatt?.close()
    }

    fun doConnectDevice(device: BluetoothDevice, uuidString: String) {
        val serviceUuid = stringToParcelUuid(uuidString) // 예시 UUID
        this.charUuid = serviceUuid.toString()
        this.uuidString = uuidString
        connectToDevice(device)
    }
}
