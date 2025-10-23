package com.example.pdfextractor

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pdfextractor.util.PdfToBitmapConverter
import kotlinx.coroutines.launch

@Composable
fun PdfScreen(context: Context){
    val coroutineScope = rememberCoroutineScope()
    val getPdfLauncher  = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){uri->
        uri?.let {
            val detail =  uri.query +  uri.host + uri.path + uri.userInfo + uri
            Toast.makeText(context, "Pdf Selected $detail", Toast.LENGTH_SHORT).show()
            coroutineScope.launch{
                val result  = PdfToBitmapConverter.convert(context,it)
                result.fold(
                    onSuccess = {bitmapList->
                        Toast.makeText(context, "Pdf Converted", Toast.LENGTH_SHORT).show()

                    },
                    onFailure = {exception->
                        Toast.makeText(context, "Pdf Not Converted", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    Column(modifier=Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        Box(modifier=Modifier.fillMaxWidth(0.96f).fillMaxHeight(0.8f)
            .clip(RoundedCornerShape(20.dp))
            .border(BorderStroke(2.dp,Color.Gray.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(20.dp)
            )
        ){
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 50.dp),
                    painter = painterResource(id = R.drawable.pdf),
                    contentDescription = null
                    )

                Text(
                    modifier = Modifier.align(Alignment.Center).padding(bottom = 150.dp),
                    text = "Upload The Pdf Of Your Health Report",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Upload The Pdf Of Your Health Report",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier=Modifier.height(8.dp))
                    Button(onClick = {
                        getPdfLauncher.launch("application/pdf")
                    }) {
                        Text(text = "Upload Pdf")
                    }
                }
            }
        }
    }
}