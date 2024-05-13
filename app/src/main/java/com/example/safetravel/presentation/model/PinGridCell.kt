package com.example.safetravel.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.safetravel.R

sealed class PinGridCell {
    data class Digit(val text: String) : PinGridCell()
    data class ButtonAction(@DrawableRes val drawableRes: Int) : PinGridCell()
    data class TextAction(@StringRes val stringRes: Int) : PinGridCell()

    companion object {
        fun getCells(): List<PinGridCell> = listOf(
            Digit(text = "1"),
            Digit(text = "2"),
            Digit(text = "3"),
            Digit(text = "4"),
            Digit(text = "5"),
            Digit(text = "6"),
            Digit(text = "7"),
            Digit(text = "8"),
            Digit(text = "9"),
            TextAction(stringRes = R.string.lbl_reset),
            Digit(text = "0"),
            ButtonAction(drawableRes = R.drawable.ic_backspace)
        )
    }
}
