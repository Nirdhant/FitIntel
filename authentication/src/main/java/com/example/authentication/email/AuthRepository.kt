package com.example.authentication.email

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepository(
    private val context: Context,
    private val credentialManager: CredentialManager = CredentialManager.create(context)
) {
    private val auth = FirebaseAuth.getInstance()
    private val webClientId = "556420334828-mui12qg1sjv4k06jc9137c3vdt86aafo.apps.googleusercontent.com"

    suspend fun signInWithGoogle(): Result<FirebaseUser> {
        return try {
            // Check for existing authorized accounts first
            val existingAccountResult = attemptExistingAccountSignIn()
            if (existingAccountResult.isSuccess) {
                return existingAccountResult
            }
            attemptNewAccountSignIn()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun attemptExistingAccountSignIn(): Result<FirebaseUser> {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(true)
            .setAutoSelectEnabled(false)
            .build()

        return performCredentialRequest(googleIdOption)
    }

    private suspend fun attemptNewAccountSignIn(): Result<FirebaseUser> {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        return performCredentialRequest(googleIdOption)
    }

    private suspend fun performCredentialRequest(googleIdOption: GetGoogleIdOption): Result<FirebaseUser> {
        return try {
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val activityContext = if (context is Activity) {
                context
            } else {
                return Result.failure(Exception("Activity context required for Google Sign-In"))
            }

            val credentialResponse = credentialManager.getCredential(
                request = request,
                context = activityContext
            )
            handleCredentialResponse(credentialResponse)
        }
        catch (e: GetCredentialException) {
            when (e) {
                is GetCredentialCancellationException -> {
                    Result.failure(Exception("User cancelled sign-in"))
                }
                is NoCredentialException -> {
                    Result.failure(Exception("No Google accounts available"))
                }
                else -> Result.failure(e)
            }
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun handleCredentialResponse(response: GetCredentialResponse): Result<FirebaseUser> {
        return when (val credential = response.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        authenticateWithFirebase(googleCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Result.failure(Exception("Invalid Google ID token", e))
                    }
                } else {
                    Result.failure(Exception("Unexpected credential type"))
                }
            }
            else -> Result.failure(Exception("Unknown credential type"))
        }
    }

    private suspend fun authenticateWithFirebase(idToken: String): Result<FirebaseUser> {
        return try {
            val user = suspendCancellableCoroutine<FirebaseUser> { continuation ->
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            auth.currentUser?.let { user ->
                                continuation.resume(user)
                            } ?: continuation.resumeWithException(
                                Exception("Authentication succeeded but user is null")
                            )
                        } else {
                            continuation.resumeWithException(
                                task.exception ?: Exception("Unknown Firebase authentication error")
                            )
                        }
                    }
            }
            Result.success(user)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
            Result.success(Unit)
        } catch (e: ClearCredentialException) {
            Result.failure(Exception("Failed to clear credentials", e))
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    fun isUserSignedIn(): Boolean = auth.currentUser != null
}
