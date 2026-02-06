package com.example.fitintel.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitintel.components.CustomCircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🔬 FitIntel Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 1.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Health Metrics Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🔬 Health Metrics",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 22.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Row 1: Hb, Sugar, Chol
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        HealthMetricItem(
                            title = "Hb",
                            value = "13.6",
                            unit = "g/dL",
                            progress = 0.85f,
                            color = Color(0xFFE91E63)
                        )
                        HealthMetricItem(
                            title = "Sugar",
                            value = "98",
                            unit = "mg/dL",
                            progress = 0.72f,
                            color = Color(0xFFFF9800)
                        )
                        HealthMetricItem(
                            title = "Chol",
                            value = "195",
                            unit = "mg/dL",
                            progress = 0.65f,
                            color = Color(0xFF2196F3)
                        )
                    }
                }
            }

            // ✅ FIXED Quick Actions - NO weight() needed
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Quick Actions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Row with proper weight scope
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // ✅ weight(1f) WORKS HERE - inside Row scope
                        QuickActionCard(
                            title = "📊 Reports",
                            description = "View history",
                            icon = "📈",
                            modifier = Modifier.weight(1f) // ✅ PERFECT
                        )
                        QuickActionCard(
                            title = "⚡ AI Advice",
                            description = "Get tips",
                            icon = "🤖",
                            modifier = Modifier.weight(1f) // ✅ PERFECT
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthMetricItem(
    title: String,
    value: String,
    unit: String,
    progress: Float,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        CustomCircularProgressIndicator(
            progress = progress,
            size = 95.dp,
            strokeWidth = 10.dp,
            trackColor = Color.White,
            progressColor = color,
            showPercentage = true
        )
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = "$value $unit",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

// ✅ UPDATED: Added modifier parameter
@Composable
fun QuickActionCard(
    title: String,
    description: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxHeight(0.5f) // ✅ No weight - passed from parent
            .aspectRatio(1f), // ✅ Perfect square cards
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = { /* TODO */ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = icon,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
