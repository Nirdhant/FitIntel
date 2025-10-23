package com.example.pdfextractor
import android.graphics.Bitmap
import com.example.pdfextractor.model.HealthData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TextExtractor {
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private fun parseHealthData(text: String): HealthData{
        val ageRegex = Regex("Age[:\\s]+(\\d+)")
        val bloodGroupRegex = Regex("Blood Group[:\\s]+([ABO][+-])")
        val bmiRegex = Regex("BMI[:\\s]+([0-9.]+)")
        val sugarRegex = Regex("Sugar Level[:\\s]+([0-9.]+)")
        val hemoglobinRegex = Regex("Hemoglobin[:\\s]+([0-9.]+)")
        val cholesterolRegex = Regex("Cholesterol[:\\s]+([0-9.]+)")

        return HealthData(
            age = ageRegex.find(text)?.groupValues?.get(1)?.toIntOrNull(),
            bloodGroup = bloodGroupRegex.find(text)?.groupValues?.get(1),
            bmi = bmiRegex.find(text)?.groupValues?.get(1)?.toFloatOrNull(),
            sugarLevel = sugarRegex.find(text)?.groupValues?.get(1)?.toFloatOrNull(),
            hemoglobinLevel = hemoglobinRegex.find(text)?.groupValues?.get(1)?.toFloatOrNull(),
            cholesterolLevel = cholesterolRegex.find(text)?.groupValues?.get(1)?.toFloatOrNull()
        )
    }

    suspend fun extractTextFromBitmap(bitmaps:List<Bitmap>): Result<HealthData> = withContext(Dispatchers.IO){
        try {
            val allText = StringBuilder()
            bitmaps.forEach {eachBitmap->
                val inputImage  = InputImage.fromBitmap(eachBitmap,0)
                val processedText = textRecognizer.process(inputImage).await()
                allText.append(processedText.text).append("\n")
            }
            val healthData = parseHealthData(allText.toString())
            Result.success(healthData)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }
    fun cleanUp(){
        textRecognizer.close()
    }
}