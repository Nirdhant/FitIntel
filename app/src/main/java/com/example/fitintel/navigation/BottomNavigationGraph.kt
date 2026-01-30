package com.example.fitintel.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitintel.screens.DashboardScreen
import com.example.fitintel.screens.SettingsScreen
import com.example.gemini.GeminiScreen
import com.example.pdfextractor.PdfScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            DashboardScreen()
        }

        composable(Screen.PdfUpload.route) {
            PdfScreen()
        }

        composable(Screen.GeminiResponse.route) {
            GeminiScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
