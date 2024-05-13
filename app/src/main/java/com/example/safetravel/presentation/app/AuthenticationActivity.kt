package com.example.safetravel.presentation.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.safetravel.presentation.components.auth.AuthenticationScreen
import com.example.safetravel.presentation.theme.SafeTravelTheme
import com.example.safetravel.presentation.viewmodel.AuthenticationViewModel
import org.koin.androidx.compose.koinViewModel

class AuthenticationActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = koinViewModel<AuthenticationViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val lifecycleOwner = LocalLifecycleOwner.current

            SafeTravelTheme {
                AnimatedVisibility(
                    visible = !uiState.isAuthenticated,
                    exit = fadeOut(tween(200))
                ) {
                    Scaffold { padding ->
                        AuthenticationScreen(
                            uiState = uiState,
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize(),
                            onSuccess = {
                                MainActivity.startActivity(this@AuthenticationActivity)
                                this@AuthenticationActivity.finish()
                            },
                            onError = { errorCode ->
                                if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                                    viewModel.markNoAuthenticationMethod()
                                }
                            },
                            onDigitClick = viewModel::enterDigit,
                            onBackspaceClick = viewModel::deletePinDigit,
                            onResetPinClick = viewModel::resetPin,
                        )
                    }

                    DisposableEffect(lifecycleOwner) {
                        onDispose {
                            if (uiState.isAuthenticated) {
                                MainActivity.startActivity(this@AuthenticationActivity)
                                this@AuthenticationActivity.finish()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun startActivity(activity: MainActivity) {
            val intent = Intent(activity, AuthenticationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }

            activity.startActivity(intent)
            activity.finish()
        }

        fun startActivity(activity: StartupChecksActivity) {
            val intent = Intent(activity, AuthenticationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }

            activity.startActivity(intent)
            activity.finish()
        }
    }
}