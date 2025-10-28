package com.example.gemini

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pdfextractor.model.HealthData


@Composable
fun GeminiScreen(healthData: HealthData) {

    val viewModel: GeminiViewModel = viewModel()
    val geminiState by viewModel.geminiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.generateHealthAdvice(healthData)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
            horizontalAlignment = Alignment.End
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                when (geminiState) {
                    is GeminiState.Initial -> {
                    }
                    is GeminiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = Color.Yellow
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Generating health advice...",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    is GeminiState.Success -> {
                        val response = (geminiState as GeminiState.Success).response
                        ResponseUi(response = response)
                    }
                    is GeminiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Text(
                                text = "Error: ${(geminiState as GeminiState.Error).message}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        ) {
            Row(modifier = Modifier
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
                    contentDescription = "Gemini logo"
                )
                Spacer(modifier = Modifier.width(3.dp))
                Image(
                    modifier = Modifier.size(90.dp),
                    painter = painterResource(id = R.drawable.gemini_text),
                    contentDescription = "Gemini text",
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }
    }
}
