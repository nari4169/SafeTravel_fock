package com.example.safetravel.presentation.app

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.safetravel.domain.isBluetoothPermissionGranted
import com.example.safetravel.presentation.theme.SafeTravelTheme
import com.example.safetravel.presentation.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeTravelTheme {
                val manager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                SafeTravelApp(
                    uiState = mainViewModel.uiState,
                    adapter = manager.adapter,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.onBluetoothPermissionResult(this.isBluetoothPermissionGranted())
    }

    companion object {
        fun startActivity(activity: StartupChecksActivity) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }

            activity.startActivity(intent)
            activity.finish()
        }
    }
}
