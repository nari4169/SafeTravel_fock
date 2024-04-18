package com.example.safetravel.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.domain.model.Device

@Composable
fun DeviceListItem(
    device: Device,
    onLockStateClicked: () -> Unit,
    onDeleteClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onCustomizeClick: () -> Unit,
    onRetryConnectionClick: () -> Unit,
) {
    val isEnabled = device.isConnected && device.isVerified
    val hasOverlay = !device.isConnected || !device.isVerified || device.isConnectionLoading
    val lockedStateDrawable = if (device.isLocked) R.drawable.ic_locked else R.drawable.ic_unlocked
    ElevatedCard(elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)) {
        Box {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(device.type.drawableRes),
                    contentDescription = null
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    Text(
                        text = device.macAddress,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilledTonalIconToggleButton(
                            checked = device.isLocked,
                            onCheckedChange = { onLockStateClicked() },
                            enabled = isEnabled,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Icon(
                                painter = painterResource(lockedStateDrawable),
                                contentDescription = null,
                            )
                        }

                        FilledTonalIconButton(
                            onClick = onCustomizeClick,
                            enabled = isEnabled,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_customize),
                                contentDescription = null,
                            )
                        }

                        FilledTonalIconButton(
                            onClick = onDeleteClick,
                            enabled = isEnabled,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }

            if (hasOverlay) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .matchParentSize()
                        .align(Alignment.Center)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
                ) {
                    when {
                        device.isConnectionLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                strokeWidth = 2.dp
                            )

                            Text(
                                text = stringResource(R.string.lbl_connecting),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        !device.isConnected -> {
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
        }
    }
}
