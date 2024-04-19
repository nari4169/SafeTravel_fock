package com.example.safetravel.presentation.components.permission

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.safetravel.R
import com.example.safetravel.domain.model.DetailedPermission
import com.example.safetravel.presentation.theme.SafeTravelTheme

@Composable
fun PermissionDialog(
    permission: DetailedPermission,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGrantClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider()
                TextButton(
                    onClick = if (isPermanentlyDeclined) onGrantClick else onOkClick
                ) {
                    Text(
                        text = if (isPermanentlyDeclined) {
                            stringResource(R.string.lbl_permission_dialog_grant)
                        } else {
                            stringResource(R.string.lbl_ok)
                        },
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        title = {
            Text(text = stringResource(R.string.lbl_permission_dialog_title))
        },
        text = {
            Text(
                text = if (isPermanentlyDeclined) {
                    stringResource(permission.declinedMessageId)
                } else {
                    stringResource(permission.genericMessageId)
                }
            )
        }
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PermissionDialogPreview() {
    SafeTravelTheme {
        PermissionDialog(
            permission = DetailedPermission.BluetoothPermission,
            isPermanentlyDeclined = false,
            onDismiss = {},
            onOkClick = {},
            onGrantClick = {},
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PermissionDialogPermanentlyDeclinedPreview() {
    SafeTravelTheme {
        PermissionDialog(
            permission = DetailedPermission.BluetoothPermission,
            isPermanentlyDeclined = true,
            onDismiss = {},
            onOkClick = {},
            onGrantClick = {},
        )
    }
}
