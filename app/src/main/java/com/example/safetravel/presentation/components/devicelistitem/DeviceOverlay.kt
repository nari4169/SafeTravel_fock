package com.example.safetravel.presentation.components.devicelistitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.safetravel.R

@Composable
fun DeviceOverlay(
    isConnected: Boolean,
    isConnectionLoading: Boolean,
    onDeleteClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onRetryConnectionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
    ) {
        when {
            isConnectionLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 2.dp
                )

                Text(
                    text = stringResource(R.string.lbl_connecting),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            !isConnected -> {
                Text(text = stringResource(R.string.lbl_device_not_connected_message))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(
                        onClick = onRetryConnectionClick,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = stringResource(R.string.lbl_retry_connection))
                    }

                    OutlinedButton(
                        onClick = onDeleteClick,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = stringResource(R.string.lbl_delete_device))
                    }
                }
            }

            else -> {
                Text(text = stringResource(R.string.lbl_device_not_verified_message))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(
                        onClick = onVerifyClick,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = stringResource(R.string.lbl_verify))
                    }

                    OutlinedButton(
                        onClick = onDeleteClick,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(text = stringResource(R.string.lbl_delete_device))
                    }
                }
            }
        }
    }
}
