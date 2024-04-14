package com.example.safetravel.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.domain.openBluetoothSettings

@Composable
fun AddDeviceScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BondedDevicesList(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = stringResource(R.string.lbl_add_device_message),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        FilledTonalButton(onClick = { context.openBluetoothSettings() }) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bluetooth_pair),
                    contentDescription = null
                )

                Text(
                    text = stringResource(R.string.lbl_pair_device),
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
