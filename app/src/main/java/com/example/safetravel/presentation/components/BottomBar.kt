package com.example.safetravel.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.domain.model.Device
import com.example.safetravel.presentation.model.DeviceType
import com.example.safetravel.presentation.theme.SafeTravelTheme
import java.util.UUID

@Composable
fun BottomBar(
    nfcDevice: Device?,
    onFloatingActionButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(R.drawable.ic_nfc),
                    contentDescription = null
                )

                Text(
                    text = nfcDevice?.let {
                        stringResource(R.string.lbl_nfc_device, it.name)
                    } ?: stringResource(R.string.lbl_no_nfc_device),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            ExtendedFloatingActionButton(
                onClick = onFloatingActionButtonClick,
                text = { Text(text = stringResource(R.string.lbl_add_device)) },
                icon = {
                    Icon(
                        modifier = Modifier.size(56.dp),
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomBarPreview() {
    SafeTravelTheme {
        BottomBar(
            nfcDevice = Device(
                macAddress = UUID.randomUUID().toString(),
                name = "Backpack",
                uuid = UUID.randomUUID().toString(),
                isConnected = false,
                isVerified = false,
                isConnectionLoading = false,
                type = DeviceType.BACKPACK
            ),
            onFloatingActionButtonClick = {}
        )
    }
}
