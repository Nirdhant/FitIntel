package com.example.fitintel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.fitintel.ui.theme.FitIntelTheme
import com.example.gemini.FirebaseGeminiLogic
import com.example.gemini.GeminiScreen
import com.example.pdfextractor.PdfScreen
import com.example.pdfextractor.model.HealthData


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseGeminiLogic.init(this)
        setContent {
            FitIntelTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    var healthData by remember { mutableStateOf<HealthData?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (healthData == null) {
            PdfScreen(
                onHealthDataExtracted = { extractedData ->
                    healthData = extractedData
                },
                context = LocalContext.current
            )
        } else {
            GeminiScreen(healthData = healthData!!)
        }
    }
}
