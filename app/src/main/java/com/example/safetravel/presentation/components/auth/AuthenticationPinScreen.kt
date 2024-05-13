package com.example.safetravel.presentation.components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.presentation.model.PinGridCell

private const val EMPTY_PIN_DIGIT_ALPHA = 0.5f
private const val PIN_DIGIT_COUNT = 4
private const val PIN_GRID_COLUMNS = 3

@Composable
fun AuthenticationPinScreen(
    hasPIN: Boolean,
    enteredPIN: String,
    isError: Boolean,
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val emptyPinDigits = PIN_DIGIT_COUNT - enteredPIN.length
    val (titleRes, subtitleRes) = when (hasPIN) {
        true -> R.string.lbl_no_authentication_method_pin to R.string.lbl_enter_pin
        false -> R.string.lbl_no_authentication_method_no_pin to R.string.lbl_setup_pin
    }

    Column(modifier = modifier.padding(16.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(subtitleRes),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                enteredPIN.forEach { digit ->
                    Text(
                        text = digit.toString(),
                        style = MaterialTheme.typography.displaySmall,
                    )
                }

                repeat(emptyPinDigits) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(
                                    alpha = EMPTY_PIN_DIGIT_ALPHA
                                )
                            )
                    )
                }
            }

            if (isError) {
                Text(
                    text = stringResource(R.string.lbl_wrong_pin),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(PIN_GRID_COLUMNS),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(PinGridCell.getCells()) { pinGridCell ->
                when (pinGridCell) {
                    is PinGridCell.Digit -> {
                        TextButton(onClick = { onDigitClick(pinGridCell.text) }) {
                            Text(
                                text = pinGridCell.text,
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                    }

                    is PinGridCell.ButtonAction -> {
                        IconButton(onClick = onBackspaceClick) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(pinGridCell.drawableRes),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                    }

                    is PinGridCell.TextAction -> {
                        TextButton(onClick = onResetClick) {
                            Text(
                                text = stringResource(pinGridCell.stringRes),
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
