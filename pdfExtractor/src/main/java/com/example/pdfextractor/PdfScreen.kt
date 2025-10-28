package com.example.pdfextractor

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pdfextractor.model.HealthData

@Composable
fun PdfScreen(context: Context, onHealthDataExtracted: (HealthData) -> Unit) {
    val viewModel: PdfViewModel = viewModel()

    val pdfState by viewModel.pdfState.collectAsState()

    val getPdfLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewModel.processPdf(context, it)
        }
    }
    LaunchedEffect(pdfState) {
        if (pdfState is PdfState.Success) {
            val healthData = (pdfState as PdfState.Success).healthData
            onHealthDataExtracted(healthData)
        }
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    BorderStroke(2.dp, Color.Gray.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 50.dp),
                    painter = painterResource(id = R.drawable.pdf),
                    contentDescription = "PDF icon"
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 150.dp),
                    text = "Upload The PDF Of Your Health Report",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (pdfState) {
                        is PdfState.Initial -> {
                            Text(
                                text = "No PDF uploaded yet",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }

                        is PdfState.ConvertingPdf -> {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Converting PDF to images...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        is PdfState.ExtractingText -> {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Extracting health data...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        is PdfState.Success -> {
                            Text(
                                text = "Health data extracted successfully!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Green
                            )
                        }
                        is PdfState.Error -> {
                            Text(
                                text = "Error: ${(pdfState as PdfState.Error).message}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Red
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            getPdfLauncher.launch("application/pdf")
                        },
                        enabled = pdfState !is PdfState.ConvertingPdf && pdfState !is PdfState.ExtractingText
                    ) {
                        Text(text = if (pdfState is PdfState.Success) "Upload Another PDF" else "Upload PDF")
                    }
                }
            }
        }
    }
}
