package com.example.safetravel.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safetravel.R
import com.example.safetravel.presentation.model.DeviceType
import com.example.safetravel.presentation.theme.SafeTravelTheme

private const val COLUMN_COUNT = 3

@Composable
fun CustomizationScreen(onDeviceTypeClick: (DeviceType) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyVerticalGrid(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            columns = GridCells.Fixed(COLUMN_COUNT),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(DeviceType.entries) {
                ElevatedCard(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable { onDeviceTypeClick(it) }
                ) {
                    Surface {
                        Icon(
                            modifier = Modifier.size(80.dp),
                            painter = painterResource(it.drawableRes),
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Text(
            text = stringResource(R.string.lbl_customize_message),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CustomizationScreenPreview() {
    SafeTravelTheme {
        CustomizationScreen(onDeviceTypeClick = {})
    }
}
