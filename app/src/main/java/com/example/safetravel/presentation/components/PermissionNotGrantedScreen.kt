package com.example.safetravel.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.safetravel.R
import com.example.safetravel.domain.openAppSettings

@Composable
fun PermissionNotGrantedScreen(modifier: Modifier) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.lbl_permission_bluetooth_not_granted_message),
            textAlign = TextAlign.Center
        )

        TextButton(
            onClick = { context.openAppSettings() }
        ) {
            Text(
                text = stringResource(R.string.lbl_permission_bluetooth_grant_redirect),
                textAlign = TextAlign.Center
            )
        }
    }
}
