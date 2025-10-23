package com.example.gemini

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

object FirebaseGeminiLogic {
    private var model: GenerativeModel? = null
    fun init(context: Context){
        FirebaseApp.initializeApp(context)
        model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.0-flash-lite-001")
    }
    suspend fun sendPrompt(prompt: String): String{

        return try {
            val response = model?.generateContent(prompt)
            response?.text?.let {
                return it
            } ?: "No response"
        }
        catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}