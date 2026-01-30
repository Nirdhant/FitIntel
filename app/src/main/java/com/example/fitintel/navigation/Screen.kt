package com.example.fitintel.navigation
import com.example.fitintel.R
sealed class Screen(
    val route: String,
    val title: String,
    val icon: Int
){
    object Home: Screen("home","Home",R.drawable.home)
    object PdfUpload: Screen("pdf_upload","Upload",R.drawable.upload_file)
    object GeminiResponse: Screen("gemini_response","Response", com.example.gemini.R.drawable.gemini)
    object Settings: Screen("settings","Settings",R.drawable.settings)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.PdfUpload,
    Screen.GeminiResponse,
    Screen.Settings
)