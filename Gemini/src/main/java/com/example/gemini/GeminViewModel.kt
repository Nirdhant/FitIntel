package com.example.gemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pdfextractor.model.HealthData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeminiViewModel : ViewModel() {

    private val _geminiState = MutableStateFlow<GeminiState>(GeminiState.Initial)
    val geminiState: StateFlow<GeminiState> = _geminiState.asStateFlow()

    fun generateHealthAdvice(healthData: HealthData) {
        viewModelScope.launch {
            try {
                _geminiState.value = GeminiState.Loading
                val prompt = buildHealthPrompt(healthData)

                val response = FirebaseGeminiLogic.sendPrompt(prompt)

                if (response.startsWith("Error:") || response == "No response") {
                    _geminiState.value = GeminiState.Error(response)
                } else {
                    _geminiState.value = GeminiState.Success(response)
                }

            } catch (e: Exception) {
                _geminiState.value = GeminiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    private fun buildHealthPrompt(healthData: HealthData): String {
        return """
            You are a professional health advisor and nutritionist. Analyze the health report and provide clear, actionable recommendations.
            
            === PATIENT HEALTH DATA ===
            Age: ${healthData.age ?: "Not provided"}
            Blood Group: ${healthData.bloodGroup ?: "Not provided"}
            BMI: ${healthData.bmi ?: "Not provided"}
            Sugar Level: ${healthData.sugarLevel?.let { "$it mg/dL" } ?: "Not provided"}
            Hemoglobin Level: ${healthData.hemoglobinLevel?.let { "$it g/dL" } ?: "Not provided"}
            Cholesterol Level: ${healthData.cholesterolLevel?.let { "$it mg/dL" } ?: "Not provided"}
            Heart Rate: ${healthData.heartRate?.let { "$it bpm" } ?: "Not provided"}
            Calories Burned: ${healthData.caloriesBurned ?: "Not provided"}
            
            === RESPONSE FORMAT (STRICTLY FOLLOW) ===
            
            🩺 **HEALTH ANALYSIS**
            • Overall Status: [Brief assessment in 1 line]
            • Abnormal Values: [List any concerning values with ranges]
            • Risk Level: [Low/Moderate/High with reason]
            
            🥗 **VEGETARIAN DIET PLAN**
            
            Breakfast:
            • [Item 1 with brief benefit]
            • [Item 2 with brief benefit]
            
            Lunch:
            • [Item 1]
            • [Item 2]
            
            Dinner:
            • [Item 1]
            • [Item 2]
            
            Snacks:
            • [Healthy snack 1]
            • [Healthy snack 2]
            
            🍗 **NON-VEGETARIAN DIET PLAN**
            
            Breakfast:
            • [Item 1 with brief benefit]
            • [Item 2 with brief benefit]
            
            Lunch:
            • [Item 1]
            • [Item 2]
            
            Dinner:
            • [Item 1]
            • [Item 2]
            
            Snacks:
            • [Protein-rich snack 1]
            • [Protein-rich snack 2]
            
            💪 **EXERCISE RECOMMENDATIONS**
            
            Daily Exercise:
            • [Exercise 1]: [Duration] - [Benefit]
            • [Exercise 2]: [Duration] - [Benefit]
            
            Frequency: [How many days per week]
            
            Precautions:
            • [Important precaution 1]
            • [Important precaution 2]
            
            ⚠️ **IMPORTANT WARNINGS**
            
            Immediate Concerns:
            • [Concern 1 if any]
            • [Concern 2 if any]
            
            When to See Doctor:
            • [Symptom 1]
            • [Symptom 2]
            
            Lifestyle Changes:
            • [Change 1]
            • [Change 2]
            
            === FORMATTING RULES ===
            1. Use bullet points (•) for ALL list items
            2. Keep each point concise (max 1-2 lines)
            3. Use emojis for section headers (🩺 🥗 🍗 💪 ⚠️)
            4. Maintain clear spacing between sections
            5. Be specific with quantities (e.g., "30 minutes", "3 times per week")
            6. Avoid long paragraphs - break into bullet points
            7. Use bold (**text**) for emphasis on important terms
            8. If a value is normal, mention it briefly
            9. If a value is abnormal, explain why and what to do
            10. Keep total response under 800 words
            
            === ADDITIONAL REQUIREMENTS ===
            • Be encouraging and positive in tone
            • Use simple, easy-to-understand language
            • Provide practical suggestions that can be followed easily
            • Focus on actionable advice over theory
            • If critical values exist, put them at the top with ⚠️
            
            Generate the response now following the EXACT format above.
        """.trimIndent()
    }

    fun resetState() {
        _geminiState.value = GeminiState.Initial
    }
}
