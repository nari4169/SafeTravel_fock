package com.example.safetravel.presentation.components.devicelistitem

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.presentation.theme.SafeTravelTheme

@Composable
fun DeviceWarningsRow(
    verifyEnabled: Boolean,
    reconnectEnabled: Boolean,
    reconnectLoading: Boolean,
    onVerifyClick: () -> Unit,
    onReconnectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        if (verifyEnabled) {
            FilledTonalButton(
                onClick = onVerifyClick,
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(8.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_warning),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = stringResource(R.string.lbl_verify))
            }
        }

        if (reconnectEnabled) {
            FilledTonalButton(
                onClick = onReconnectClick,
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(8.dp),
                enabled = !reconnectLoading,
            ) {
                if (reconnectLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_warning),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (reconnectLoading) {
                        stringResource(R.string.lbl_connecting)
                    } else {
                        stringResource(R.string.lbl_reconnect)
                    }
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DeviceWarningsRowPreview() {
    SafeTravelTheme {
        DeviceWarningsRow(
            verifyEnabled = true,
            reconnectEnabled = true,
            reconnectLoading = true,
            onVerifyClick = {},
            onReconnectClick = {}
        )
    }
}
