package com.example.safetravel.presentation.components.permission

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
import com.example.safetravel.R
import com.example.safetravel.domain.model.DetailedPermission

@Composable
fun PermissionDialog(
    permission: DetailedPermission,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGrantClick: () -> Unit,
    modifier: Modifier,
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
