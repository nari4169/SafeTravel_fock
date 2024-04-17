package com.example.safetravel.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.domain.model.Device
import com.example.safetravel.domain.thenIf

@Composable
fun DeviceListItem(
    device: Device,
    enabled: Boolean,
    onLockStateClicked: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val lockedStateDrawable = if (device.isLocked) R.drawable.ic_locked else R.drawable.ic_unlocked
    ElevatedCard {
        ListItem(
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_bag_1),
                    contentDescription = null
                )
            },
            headlineContent = {
                Row(
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = device.name)
                    Text(text = device.macAddress)
                }
            },
            supportingContent = {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(
                        painter = painterResource(lockedStateDrawable),
                        contentDescription = null,
                        modifier = Modifier.thenIf(enabled) {
                            clickable { onLockStateClicked() }
                        }
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier.thenIf(enabled) {
                            clickable { onDeleteClick() }
                        }
                    )
                }
            }
        )
    }
}
