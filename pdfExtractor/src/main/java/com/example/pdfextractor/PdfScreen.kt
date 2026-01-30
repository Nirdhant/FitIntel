package com.example.pdfextractor

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.state.AppState
import com.example.pdfextractor.util.PdfToBitmapConverter
import kotlinx.coroutines.launch

@Composable
fun PdfScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val textExtractor = remember { TextExtractor() }

    val getPdfLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // ✅ Start global loading
            AppState.startProcessing("Converting PDF...")
            Toast.makeText(context, "PDF Selected", Toast.LENGTH_SHORT).show()

            coroutineScope.launch {
                val result = PdfToBitmapConverter.convert(context, it)
                result.fold(
                    onSuccess = { bitmapList ->
                        AppState.startProcessing("Extracting text...")
                        Toast.makeText(context, "PDF Converted", Toast.LENGTH_SHORT).show()

                        val extractResult = textExtractor.extractTextFromBitmap(bitmapList)
                        extractResult.fold(
                            onSuccess = { healthData ->
                                AppState.startProcessing("Analyzing health data...")

                                // ✅ Save to AppState
                                AppState.setHealthData(healthData)

                                // Small delay for UX
                                kotlinx.coroutines.delay(500)

                                // ✅ Stop loading
                                AppState.stopProcessing()

                                // ✅ Show success toast - user navigates manually
                                Toast.makeText(
                                    context,
                                    "✅ Analysis complete! Click 'Response' tab to view results.",
                                    Toast.LENGTH_LONG
                                ).show()

                                // ❌ NO auto-navigation - removed onSuccess(healthData)
                            },
                            onFailure = { exception ->
                                AppState.stopProcessing()
                                Toast.makeText(
                                    context,
                                    "Extraction failed: ${exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    onFailure = { exception ->
                        AppState.stopProcessing()
                        Toast.makeText(
                            context,
                            "PDF Conversion failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
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
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 150.dp),
                    text = "Upload The Pdf Of Your Health Report",
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
                    Button(
                        onClick = {
                            // ✅ Reset previous data before new upload
                            AppState.reset()
                            getPdfLauncher.launch("application/pdf")
                        },
                        enabled = !AppState.isProcessing  // ✅ Disabled during processing
                    ) {
                        Text(text = "Upload Pdf")
                    }
                }
            }
        }
    }
}
