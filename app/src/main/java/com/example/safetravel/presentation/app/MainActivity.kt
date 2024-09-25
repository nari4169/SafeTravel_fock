package com.example.safetravel.presentation.app

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safetravel.R
import com.example.safetravel.domain.isBluetoothPermissionGranted
import com.example.safetravel.domain.model.BluetoothStatus
import com.example.safetravel.domain.openBluetoothSettings
import com.example.safetravel.presentation.components.permission.PermissionNotGrantedScreen
import com.example.safetravel.presentation.theme.SafeTravelTheme
import com.example.safetravel.presentation.utils.BluetoothHelper
import com.example.safetravel.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    lateinit var bluetoothHelper : BluetoothHelper

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothHelper = BluetoothHelper(this@MainActivity, mainViewModel)
//        if (ActivityCompat.checkSelfPermission(
//                this@MainActivity,
//                Manifest.permission.BLUETOOTH_SCAN
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            bluetoothHelper.startScan()
//        }

        setContent {
            SafeTravelTheme {
                val coroutineScope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                val bluetoothOffSnackbar = stringResource(R.string.lbl_bluetooth_off_snackbar)
                val snackbarAction = stringResource(R.string.lbl_enable)
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                var bondedDevices by remember { mutableStateOf<List<BluetoothDevice>>(emptyList()) }
                val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(uiState.bluetoothStatus) {
                    when (uiState.bluetoothStatus) {
                        BluetoothStatus.ON -> snackbarHostState.currentSnackbarData?.dismiss()
                        BluetoothStatus.OFF -> {
                            coroutineScope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = bluetoothOffSnackbar,
                                    actionLabel = snackbarAction,
                                    withDismissAction = true,
                                )

                                when (result) {
                                    SnackbarResult.ActionPerformed -> openBluetoothSettings()
                                    SnackbarResult.Dismissed -> Unit
                                }
                            }
                        }
                    }
                }

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            // Get the phone's paired devices again on ON_RESUME and remove
                            // the ones that are no longer paired from the database
                            // ON_RESUME에서 휴대폰의 페어링된 장치를 다시 가져오고 제거합니다.
                            // 데이터베이스에서 더 이상 페어링되지 않은 항목
                            val service = context.getSystemService(Context.BLUETOOTH_SERVICE)
                            val adapter = (service as BluetoothManager).adapter
                            bondedDevices = adapter.bondedDevices.toList()
                            mainViewModel.bluetoothDevices.forEach{
                                bondedDevices = bondedDevices + it
                                Log.e("", "add= ${it.name}")
                            }
                            mainViewModel.reconcileDevices(bondedDevices.mapNotNull { it.address })
                        }
                    }

                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
                    when {
                        !this.isBluetoothPermissionGranted() -> PermissionNotGrantedScreen(
                            modifier = Modifier.padding(padding)
                        )

                        else -> SafeTravelApp(
                            viewModel = mainViewModel,
                            bondedDevices = bondedDevices,
                            modifier = Modifier.padding(padding),
                            doFindBluetooth = {
                                if (ActivityCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.BLUETOOTH_SCAN
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    bluetoothHelper.startScan()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        bluetoothHelper.stopScan()
        AuthenticationActivity.startActivity(this@MainActivity)
        this@MainActivity.finish()
    }

    companion object {
        fun startActivity(activity: AuthenticationActivity) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            activity.startActivity(intent)
            activity.finish()
        }
    }
}
