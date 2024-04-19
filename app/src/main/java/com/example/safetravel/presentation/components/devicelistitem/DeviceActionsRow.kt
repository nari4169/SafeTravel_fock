package com.example.safetravel.presentation.components.devicelistitem

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.presentation.theme.SafeTravelTheme

@Composable
fun DeviceActionsRow(
    isLocked: Boolean,
    actionsEnabled: Boolean,
    onLockStateClicked: () -> Unit,
    onCustomizeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onRenameClick: () -> Unit,
) {
    val lockedStateDrawable = if (isLocked) R.drawable.ic_locked else R.drawable.ic_unlocked

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilledTonalIconToggleButton(
            checked = isLocked,
            onCheckedChange = { onLockStateClicked() },
            enabled = actionsEnabled,
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                painter = painterResource(lockedStateDrawable),
                contentDescription = null,
            )
        }

        FilledTonalIconButton(
            onClick = onCustomizeClick,
            enabled = actionsEnabled,
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_customize),
                contentDescription = null,
            )
        }

        FilledTonalIconButton(
            onClick = onRenameClick,
            enabled = actionsEnabled,
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_rename),
                contentDescription = null,
            )
        }

        FilledTonalIconButton(
            onClick = onDeleteClick,
            enabled = actionsEnabled,
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DeviceActionsRowPreview() {
    SafeTravelTheme {
        DeviceActionsRow(
            isLocked = false,
            actionsEnabled = true,
            onLockStateClicked = {},
            onCustomizeClick = {},
            onDeleteClick = {},
            onRenameClick = {}
        )
    }
}
