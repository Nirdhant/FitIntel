package com.example.fitintel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.authentication.email.AuthScreen
import com.example.authentication.email.AuthViewModel
import com.example.fitintel.ui.theme.FitIntelTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
       // FirebaseGeminiLogic.init(this)
        setContent {
            FitIntelTheme {
                AuthScreen(this, authViewModel) {
                    Toast.makeText(this,"AuthSuccess", Toast.LENGTH_LONG).show()
                }
//                CustomCircularProgressIndicator(
//                    indicatorSize = 200.dp,
//                    strokeWidth = 20.dp,
//                    progressColor = Color.Red,
//                    trackColor = Color.White,
//                    gapSize =0.dp,
//                    strokeCap = StrokeCap.Butt,
//                    reportName="8L/Min",
//                    reportNameColor=Color.White,
//                    progress = { 0.5f }
//                )
                //GeminiScreen()
            }
        }
    }
}
