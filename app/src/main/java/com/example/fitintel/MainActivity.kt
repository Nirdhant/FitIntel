package com.example.fitintel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.core.state.AppState
import com.example.fitintel.components.BottomNavigationBar
import com.example.fitintel.navigation.BottomNavGraph
import com.example.fitintel.screens.authentication.AuthNavigation
import com.example.fitintel.ui.theme.FitIntelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitIntelTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // ✅ FIXED: Correct State collection
    val isAuthenticated by AppState.isAuthenticated

    if (!isAuthenticated) {
        // Show Auth screens FIRST
        AuthNavigation(
            onAuthSuccess = {
                // On successful login/signup → Go to main app
                AppState.setAuthenticated(true)
            }
        )
    } else {
        // Show main app with bottom nav
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    BottomNavGraph(navController = navController)
                }
            }

            // Global Loading Overlay
            if (AppState.isProcessing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = Color.White,
                            strokeWidth = 6.dp
                        )
                        Text(
                            text = AppState.processingMessage,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
