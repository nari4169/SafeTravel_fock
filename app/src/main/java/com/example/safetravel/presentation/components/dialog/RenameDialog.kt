package com.example.safetravel.presentation.components.dialog

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.safetravel.R
import com.example.safetravel.presentation.theme.SafeTravelTheme

@Composable
fun RenameDialog(
    deviceName: String,
    onDismiss: () -> Unit,
    onRename: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.lbl_rename_dialog_title)) },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(text = deviceName) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_rename),
                        contentDescription = null
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onDismiss()
                        onRename(text)
                    }
                ),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    keyboardController?.hide()
                    onDismiss()
                    onRename(text)
                }
            ) {
                Text(text = stringResource(R.string.lbl_rename))
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
private fun RenameDialogPreview() {
    SafeTravelTheme {
        RenameDialog(
            deviceName = "Device Name",
            onRename = {},
            onDismiss = {}
        )
    }
}
