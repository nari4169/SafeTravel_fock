package com.example.safetravel.presentation.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.safetravel.R

@SuppressLint("MissingPermission")
@Composable
fun BondedDevicesList(
    bondedDevices: List<BluetoothDevice>,
    onDeviceClick: (BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(bondedDevices) { device ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onDeviceClick(device) }
            ) {
                Text(text = device.name, modifier = Modifier.weight(1f))
                Icon(painter = painterResource(R.drawable.ic_link), contentDescription = null)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        }

        item {
            if (bondedDevices.isEmpty()) {
                Text(
                    text = stringResource(R.string.lbl_no_paired_devices),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
