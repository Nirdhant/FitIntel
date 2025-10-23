package com.example.geminitest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gemini.FirebaseGeminiLogic
import com.example.gemini.GeminiScreen
import com.example.geminitest.ui.theme.FitIntelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseGeminiLogic.init(this)
        setContent {
            FitIntelTheme {
                GeminiScreen()
            }
        }
    }
}
