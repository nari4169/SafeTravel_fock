package com.example.safetravel.domain.model

import androidx.annotation.DrawableRes
import com.example.safetravel.R

enum class LockStatus(val id: Int, @DrawableRes val drawableRes: Int) {
    UNKNOWN(0, R.drawable.ic_unknown),
    LOCKED(1, R.drawable.ic_locked),
    UNLOCKED(2, R.drawable.ic_unlocked);

    companion object {
        fun getById(id: Int) = entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}
