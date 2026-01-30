package com.example.gemini

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.HealthData
import com.example.core.state.AppState
import kotlinx.coroutines.launch

@Composable
fun GeminiScreen() {
    val coroutineScope = rememberCoroutineScope()

    // ✅ Load response only once if not cached
    LaunchedEffect(AppState.healthData) {
        // Only fetch if we have data but no response yet
        if (AppState.healthData != null && AppState.geminiResponse == null) {
            AppState.startProcessing("Generating AI response...")

            coroutineScope.launch {
                val prompt = buildHealthPrompt(AppState.healthData!!)
                val response = FirebaseGeminiLogic.sendPrompt(prompt)

                // ✅ Cache response
                AppState.setGeminiResponse(response)
                AppState.stopProcessing()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // ✅ Show cached response or placeholder
        if (AppState.geminiResponse != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp)
            ) {
                item {
                    ResponseUi(AppState.geminiResponse!!)
                }
            }
        } else if (AppState.healthData == null) {
            // ✅ No data uploaded yet
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "📄",
                        fontSize = 64.sp
                    )
                    Text(
                        text = "No health report uploaded",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Please upload a PDF to see AI analysis",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Bottom Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp)
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.gemini),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(3.dp))

                Image(
                    modifier = Modifier.size(90.dp),
                    painter = painterResource(id = R.drawable.gemini_text),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }
    }
}

private fun buildHealthPrompt(healthData: HealthData): String {
    return buildString {
        append("Analyze this health report and provide detailed advice:\n\n")

        healthData.age?.let { append("Age: $it years\n") }
        healthData.bloodGroup?.let { append("Blood Group: $it\n") }
        healthData.bmi?.let { append("BMI: $it\n") }
        healthData.sugarLevel?.let { append("Sugar Level: $it mg/dL\n") }
        healthData.hemoglobinLevel?.let { append("Hemoglobin: $it g/dL\n") }
        healthData.cholesterolLevel?.let { append("Cholesterol: $it mg/dL\n") }
        healthData.heartRate?.let { append("Heart Rate: $it bpm\n") }
        healthData.caloriesBurned?.let { append("Calories: $it\n") }

        append("\nProvide:\n")
        append("1. Health Status Assessment\n")
        append("2. Vegetarian Diet Plan\n")
        append("3. Non-Vegetarian Diet Plan\n")
        append("4. Exercise Recommendations\n")
        append("5. Health Warnings (if any)\n")
    }
}
