package com.example.pdfextractor.model

data class HealthData(
    val age:Int? = null,
    val bloodGroup : String? = null,
    val bmi : Float? = null,
    val sugarLevel : Float? = null,
    val hemoglobinLevel : Float? = null,
    val cholesterolLevel : Float? = null,
    val heartRate : Int? = null,
    val caloriesBurned : Int? = null
)
