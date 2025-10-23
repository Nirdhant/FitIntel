package com.example.authenticationtest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.authentication.email.AuthScreen
import com.example.authentication.email.AuthViewModel
import com.example.authenticationtest.ui.theme.FitIntelTheme

class MainActivity : ComponentActivity() {
    val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitIntelTheme {
                AuthScreen(this,authViewModel) {
                    Toast.makeText(this,"AuthSuccess", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

