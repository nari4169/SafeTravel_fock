package com.example.safetravel.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.domain.model.Device

@Composable
fun VerificationAlertDialog(
    device: Device,
    onDismiss: () -> Unit,
    onVerificationSuccessful: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.lbl_verify_device)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(R.string.lbl_verify_message),
                    style = MaterialTheme.typography.bodySmall
                )
                TextField(
                    value = text,
                    isError = isError,
                    singleLine = true,
                    onValueChange = {
                        isError = false
                        text = it
                    },
                    placeholder = { Text(text = stringResource(R.string.lbl_device_id)) },
                    supportingText = {
                        if (isError) Text(text = stringResource(R.string.lbl_wrong_device_id))
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_password),
                            contentDescription = null
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (text == device.uuid) {
                                keyboardController?.hide()
                                onVerificationSuccessful()
                                onDismiss()
                            } else {
                                isError = true
                            }
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        errorSupportingTextColor = MaterialTheme.colorScheme.error,
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (text == device.uuid) {
                        keyboardController?.hide()
                        onVerificationSuccessful()
                        onDismiss()
                    } else {
                        isError = true
                    }
                }
            ) {
                Text(text = stringResource(R.string.lbl_verify))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.lbl_cancel))
            }
        }
    )
}
