package com.example.safetravel.presentation.components.permission

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.safetravel.R
import com.example.safetravel.domain.openAppSettings
import com.example.safetravel.presentation.theme.SafeTravelTheme

@Composable
fun PermissionNotGrantedScreen(modifier: Modifier = Modifier) {
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PermissionNotGrantedScreenPreview() {
    SafeTravelTheme {
        PermissionNotGrantedScreen()
    }
}
