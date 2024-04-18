package com.example.safetravel.presentation.model

import androidx.annotation.DrawableRes
import com.example.safetravel.R

enum class DeviceType(val id: Int, @DrawableRes val drawableRes: Int) {
    BACKPACK(0, R.drawable.ic_backpack),
    PURSE(1, R.drawable.ic_purse),
    SUITCASE_REGULAR(2, R.drawable.ic_suitcase_regular),
    SUITCASE_MINIMALISTIC(3, R.drawable.ic_suitcase_minimalistic),
    SUITCASE_BUSINESS(4, R.drawable.ic_suitcase_business),
    SUITCASE_MEDICAL(5, R.drawable.ic_suitcase_medical),
    SUITCASE_VALUABLE(6, R.drawable.ic_suitcase_valuable),
    SUITCASE_DESIGNED(7, R.drawable.ic_suitcase_designed),
    SUITCASE_TRAVEL(8, R.drawable.ic_suitcase_travel);

    companion object {
        fun getById(id: Int) = entries.firstOrNull { it.id == id } ?: SUITCASE_REGULAR
    }
}
