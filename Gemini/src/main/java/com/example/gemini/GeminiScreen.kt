package com.example.gemini

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GeminiScreen(){
    val response = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        response.value = FirebaseGeminiLogic.sendPrompt("Hello how are you?")
    }
    Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End) {
            item {
                ResponseUi(response.value)
            }
            }
        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp)
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
