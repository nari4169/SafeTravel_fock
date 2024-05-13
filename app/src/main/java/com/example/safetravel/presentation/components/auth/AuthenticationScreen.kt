package com.example.safetravel.presentation.components.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.safetravel.R
import com.example.safetravel.presentation.viewmodel.model.AuthenticationUiState

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AuthenticationScreen(
    uiState: AuthenticationUiState,
    onSuccess: () -> Unit,
    onError: (Int) -> Unit,
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onResetPinClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as FragmentActivity
    val promptTitle = stringResource(R.string.lbl_authentication_prompt_title)
    val promptDescription = stringResource(R.string.lbl_authentication_prompt_description)

    when (uiState.hasAuthenticationMethod) {
        false -> {
            showAuthenticationPrompt(
                promptTitle = promptTitle,
                promptDescription = promptDescription,
                activity = activity,
                onSuccess = onSuccess,
                onError = onError
            )

            AuthenticationCanceledScreen(
                modifier = modifier,
                onClick = {
                    showAuthenticationPrompt(
                        promptTitle = promptTitle,
                        promptDescription = promptDescription,
                        activity = activity,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }
            )
        }

        true -> AuthenticationPinScreen(
            modifier = modifier,
            hasPIN = uiState.hasPIN,
            enteredPIN = uiState.enteredPIN,
            onDigitClick = onDigitClick,
            onBackspaceClick = onBackspaceClick,
            onResetClick = onResetPinClick
        )
    }
}

internal fun showAuthenticationPrompt(
    promptTitle: String,
    promptDescription: String,
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onError: (Int) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(promptTitle)
        .setDescription(promptDescription)
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errorCode)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }
        }
    )

    biometricPrompt.authenticate(promptInfo)
}
