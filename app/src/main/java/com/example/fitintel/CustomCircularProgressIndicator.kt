package com.example.fitintel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomCircularProgressIndicator(
    indicatorSize: Dp,
    strokeWidth: Dp,
    progressColor: Color,
    trackColor: Color,
    gapSize: Dp,
    strokeCap: StrokeCap,
    reportName:String,
    reportNameColor:Color,
    progress:()-> Float
){
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.size(indicatorSize).clip(RoundedCornerShape(30.dp)).background(Color.Blue).padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(indicatorSize),
                strokeWidth = strokeWidth,
                color = progressColor,
                trackColor = trackColor,
                gapSize = gapSize,
                strokeCap = strokeCap,
                progress = progress
            )
            Box(modifier = Modifier.size(indicatorSize/3).padding(1.dp).clip(RoundedCornerShape(40.dp)).background(Color.Black),
                contentAlignment = Alignment.Center) {
                Text(text = reportName, color = reportNameColor)
            }
        }
    }

}