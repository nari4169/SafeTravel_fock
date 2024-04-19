package com.example.safetravel.presentation.components.dialog

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.safetravel.R
import com.example.safetravel.presentation.theme.SafeTravelTheme

@Composable
fun DeleteDialog(
    deviceName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(text = stringResource(R.string.lbl_delete_device))
        },
        text = {
            Text(text = stringResource(R.string.lbl_delete_dialog_message, deviceName))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.lbl_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.lbl_cancel))
            }
        }
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DeleteDialogPreview() {
    SafeTravelTheme {
        DeleteDialog(
            deviceName = "Device Name",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
