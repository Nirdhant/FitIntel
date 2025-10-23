package com.example.pdftest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pdfextractor.PdfScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _root_ide_package_.com.example.pdftest.ui.theme.FitIntelTheme {
                PdfScreen(this)
            }
        }
    }
}

