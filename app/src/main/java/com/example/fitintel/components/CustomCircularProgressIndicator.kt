package com.example.fitintel.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float = 0.85f,
    size: Dp = 100.dp,
    strokeWidth: Dp = 12.dp,
    trackColor: Color = Color.White,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    showPercentage: Boolean = true,
    animateProgress: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000)
        )
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = if (animateProgress) {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        } else {
            tween(0)
        }
    )

    Box(
        modifier = modifier
            .size(size)
            .shadow(16.dp, CircleShape, ambientColor = Color.Black.copy(alpha = 0.25f))
            .background(Color.White, CircleShape)
            .clip(CircleShape)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val radius = (size.toPx() / 2f) - strokeWidthPx
            val center = Offset(size.toPx() / 2f, size.toPx() / 2f)

            // 1. Rotating glossy background track (NO radius lines)
            drawGlossyTrack(
                center = center,
                radius = radius,
                strokeWidthPx = strokeWidthPx,
                trackColor = trackColor
            )

            // 2. Smooth progress arc (NO radius lines - PERFECT!)
            val sweepAngle = 360f * animatedProgress.coerceIn(0f, 1f)
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                topLeft = Offset(center.x - radius, center.y - radius), // ✅ Precise positioning
                size = Size(radius * 2, radius * 2), // ✅ Exact size
                useCenter = false, // ✅ NO RADIUS LINES!
                style = Stroke(strokeWidthPx, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // 3. Progress glow effect (NO radius lines)
            drawArc(
                color = progressColor.copy(alpha = 0.4f),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                topLeft = Offset(center.x - radius + 2, center.y - radius + 2),
                size = Size(radius * 2, radius * 2),
                useCenter = false, // ✅ NO RADIUS LINES!
                style = Stroke(strokeWidthPx * 0.7f, cap = StrokeCap.Round)
            )
        }

        if (showPercentage) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = progressColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun DrawScope.drawGlossyTrack(
    center: Offset,
    radius: Float,
    strokeWidthPx: Float,
    trackColor: Color
) {
    // Outer glossy ring (FULL CIRCLE - NO radius lines)
    val outerGradient = Brush.radialGradient(
        0f to trackColor.copy(alpha = 0.95f),
        0.4f to trackColor.copy(alpha = 0.8f),
        0.8f to trackColor.copy(alpha = 0.5f),
        1f to trackColor.copy(alpha = 0.2f),
        center = Offset(center.x, center.y - radius * 0.3f)
    )

    drawArc(
        brush = outerGradient,
        startAngle = 0f,
        sweepAngle = 360f,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2, radius * 2),
        useCenter = false, // ✅ NO RADIUS LINES!
        style = Stroke(strokeWidthPx * 0.8f, cap = StrokeCap.Round)
    )

    // Subtle inner ring for depth
    drawArc(
        color = trackColor.copy(alpha = 0.2f),
        startAngle = 0f,
        sweepAngle = 360f,
        topLeft = Offset(center.x - radius * 0.7f, center.y - radius * 0.7f),
        size = Size(radius * 1.4f, radius * 1.4f),
        useCenter = false, // ✅ NO RADIUS LINES!
        style = Stroke(strokeWidthPx * 0.3f, cap = StrokeCap.Round)
    )
}
