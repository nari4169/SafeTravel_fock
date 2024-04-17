package com.example.safetravel.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.safetravel.domain.model.Device

@Composable
fun DevicesScreen(
    devices: List<Device>,
    onDeviceLockStateClicked: (String) -> Unit,
    onDeviceDeleteClicked: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(devices, { it.macAddress }) { device ->
            DeviceListItem(
                device = device,
                enabled = true,
                onLockStateClicked = { onDeviceLockStateClicked(device.macAddress) },
                onDeleteClick = { onDeviceDeleteClicked(device.macAddress) }
            )
        }
    }
}
