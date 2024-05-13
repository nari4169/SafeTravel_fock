package com.example.safetravel.presentation.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.safetravel.presentation.components.auth.AuthenticationScreen
import com.example.safetravel.presentation.theme.SafeTravelTheme

class AuthenticationActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeTravelTheme {
                Scaffold { padding ->
                    AuthenticationScreen(
                        modifier = Modifier.padding(padding),
                        onSuccess = {
                            MainActivity.startActivity(this@AuthenticationActivity)
                            this@AuthenticationActivity.finish()
                        }
                    )
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