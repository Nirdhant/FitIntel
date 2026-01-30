package com.example.pdfextractor

import com.example.core.model.HealthData


sealed class PdfState {
    object Initial : PdfState()
    object ConvertingPdf : PdfState()
    object ExtractingText : PdfState()
    data class Success(val healthData: HealthData) : PdfState()
    data class Error(val message: String) : PdfState()
}
