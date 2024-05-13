package com.example.safetravel.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.safetravel.presentation.viewmodel.model.AuthenticationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthenticationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState = _uiState.asStateFlow()

    fun markNoAuthenticationMethod() {
        _uiState.update { it.copy(hasAuthenticationMethod = false) }
    }

    fun enterDigit(value: String) {
        val currentPIN = _uiState.value.enteredPIN
        if (currentPIN.length < PIN_DIGIT_COUNT) {
            _uiState.update {
                it.copy(enteredPIN = currentPIN + value)
            }

            if (_uiState.value.enteredPIN.length == PIN_DIGIT_COUNT) {

            }
        }
    }

    fun deletePinDigit() {
        val currentPIN = _uiState.value.enteredPIN
        if (currentPIN.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    enteredPIN = currentPIN.take(currentPIN.length - 1),
                    isError = false
                )
            }
        }
    }

    fun resetPin() {
        _uiState.update {
            it.copy(
                enteredPIN = EMPTY_PIN,
                isError = false
            )
        }
    }

    companion object {
        private const val PIN_DIGIT_COUNT = 4
        private const val EMPTY_PIN = ""
    }
}
