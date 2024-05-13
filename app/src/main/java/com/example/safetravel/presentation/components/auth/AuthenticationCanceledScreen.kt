package com.example.safetravel.presentation.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.safetravel.R

@Composable
fun AuthenticationCanceledScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.lbl_authentication_screen_message),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        TextButton(onClick = onClick) {
            Text(
                text = stringResource(R.string.lbl_authenticate),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
