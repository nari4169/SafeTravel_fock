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

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AuthenticationScreen(
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as FragmentActivity
    val promptTitle = stringResource(R.string.lbl_authentication_prompt_title)
    val promptSubtitle = stringResource(R.string.lbl_authentication_prompt_subtitle)
    val promptDescription = stringResource(R.string.lbl_authentication_prompt_description)

    showAuthenticationPrompt(
        promptTitle = promptTitle,
        promptSubtitle = promptSubtitle,
        promptDescription = promptDescription,
        activity = activity,
        onSuccess = {},
        onError = {},
        onFailed = {}
    )
}

internal fun showAuthenticationPrompt(
    promptTitle: String,
    promptSubtitle: String,
    promptDescription: String,
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onError: () -> Unit,
    onFailed: () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(promptTitle)
        .setSubtitle(promptSubtitle)
        .setDescription(promptDescription)
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed()
            }
        }
    )

    biometricPrompt.authenticate(promptInfo)
}
