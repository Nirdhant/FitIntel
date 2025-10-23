package com.example.gemini

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResponseUi(response: String){
    Row(modifier = Modifier.fillMaxWidth(0.8f).padding(top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.Yellow)) {
            Text(response)
        }
        Box(modifier = Modifier.weight(0.1f).height(8.dp).clip(shape = RoundedCornerShape(5.dp)).background(color = Color.Black)
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.3f).height(8.dp).clip(shape = RoundedCornerShape(15.dp)).background(color = Color.Red)
              .align(Alignment.CenterEnd)
            ){
            }
        }
    }
}


