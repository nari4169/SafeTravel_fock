package com.example.safetravel.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.safetravel.domain.model.Device

@Composable
fun DevicesScreen(
    devices: List<Device>,
    onDeviceClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(devices, { it.macAddress }) { device ->
            Text(
                text = "${device.name} - ${device.macAddress}",
                modifier = Modifier.clickable { onDeviceClick(device.macAddress) }
            )
        }
    }
}
