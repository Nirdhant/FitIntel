package com.example.authentication.email

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object NotAuthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}