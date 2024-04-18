package com.example.safetravel.presentation.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.safetravel.R

private const val ITEMS_PER_PAGE = 3

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("MissingPermission")
@Composable
fun BondedDevicesList(
    bondedDevices: List<BluetoothDevice>,
    onDeviceClick: (BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = bondedDevices.chunked(ITEMS_PER_PAGE)
    val pagerState = rememberPagerState { pages.count() }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(end = 24.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.animateContentSize()
        ) { pageIndex ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val currentPage = pages[pageIndex]
                for (device in currentPage) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { onDeviceClick(device) }
                    ) {
                        Text(text = device.name, modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.ic_link),
                            contentDescription = null
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }

        if (bondedDevices.isEmpty()) {
            Text(
                text = stringResource(R.string.lbl_no_newly_paired_devices),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
