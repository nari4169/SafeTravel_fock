package com.example.safetravel.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safetravel.domain.usecase.GetAuthenticationPinUseCase
import com.example.safetravel.domain.usecase.SaveAuthenticationPinUseCase
import com.example.safetravel.presentation.viewmodel.model.AuthenticationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val getAuthenticationPinUseCase: GetAuthenticationPinUseCase,
    private val saveAuthenticationPinUseCase: SaveAuthenticationPinUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAuthenticationPinUseCase().collectLatest { pin ->
                _uiState.update { it.copy(hasPIN = pin.isNotEmpty()) }
            }
        }
    }

    private fun handleEnteredPin() {
        val currentPIN = _uiState.value.enteredPIN
        if (currentPIN.length == PIN_DIGIT_COUNT) {
            viewModelScope.launch {
                val savedPin = getAuthenticationPinUseCase().first()
                when {
                    savedPin.isEmpty() -> {
                        saveAuthenticationPinUseCase(currentPIN)
                        _uiState.update { it.copy(isAuthenticated = true) }
                    }

                    else -> {
                        _uiState.update {
                            it.copy(
                                isError = savedPin != currentPIN,
                                isAuthenticated = savedPin == currentPIN
                            )
                        }
                    }
                }
            }
        }
    }

    fun markNoAuthenticationMethod() {
        _uiState.update { it.copy(hasAuthenticationMethod = false) }
    }

    fun enterDigit(value: String) {
        val currentPIN = _uiState.value.enteredPIN
        if (currentPIN.length < PIN_DIGIT_COUNT) {
            _uiState.update { it.copy(enteredPIN = currentPIN + value) }
            handleEnteredPin()
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
