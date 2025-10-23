package com.example.authentication.email

data class UserProfile(
val uid: String,
val email: String,
val displayName: String,
val photoUrl: String?
)
