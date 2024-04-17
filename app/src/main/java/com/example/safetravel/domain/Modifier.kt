package com.example.safetravel.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Modifier.thenIf(predicate: Boolean, action: @Composable Modifier.() -> Modifier) =
    if (predicate) then(action()) else this
