package com.example.fitintel.screens.authentication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AuthNavigation(
    navController: NavHostController = rememberNavController(),
    onAuthSuccess: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = onAuthSuccess
            )
        }
        composable("signup") {
            SignUpScreen(
                navController = navController,
                onSignUpSuccess = onAuthSuccess
            )
        }
    }
}
