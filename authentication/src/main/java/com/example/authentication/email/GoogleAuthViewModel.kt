package com.example.authentication.email

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    private var _authRepository: AuthRepository?=null
    // Thread safety for repository initialization
    private val repositoryLock = Any()

    private fun getAuthRepository(context: Context): AuthRepository{
        return _authRepository ?: synchronized(repositoryLock) {
            _authRepository ?: AuthRepository(context).also {
                _authRepository = it
                checkAuthState()
            }
        }
    }
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    fun initialize(context: Context){
        if (_authRepository == null){
            getAuthRepository(context)
        }
    }

    private fun checkAuthState() {
        val currentUser = _authRepository?.getCurrentUser()
        if (currentUser != null) {
            _authState.value = AuthState.Authenticated
            _userProfile.value = UserProfile(
                uid = currentUser.uid,
                email = currentUser.email ?: "",
                displayName = currentUser.displayName ?: "",
                photoUrl = currentUser.photoUrl?.toString()
            )
        }
        else {
            _authState.value = AuthState.NotAuthenticated
        }
    }
    fun signInWithGoogle(context: Context) {                   //why using context here
        val repository = getAuthRepository(context)
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = repository.signInWithGoogle()
                if (result.isSuccess) {
                    val user = result.getOrThrow()
                    _userProfile.value = UserProfile(
                        uid = user.uid,
                        email = user.email ?: "",
                        displayName = user.displayName ?: "",
                        photoUrl = user.photoUrl?.toString()
                    )
                    _authState.value = AuthState.Authenticated
                }
                else {
                    _authState.value = AuthState.Error(
                        result.exceptionOrNull()?.message ?: "sign in failed"
                    )
                }
            }
            catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error could not sign in")
            }
        }
    }

    fun signOut(context: Context) {
        val repository = getAuthRepository(context)
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.signOut()

            if (result.isSuccess) {
                _authState.value = AuthState.NotAuthenticated
                _userProfile.value = null
                Toast.makeText(context,"Signed out",Toast.LENGTH_LONG).show()
            }
            else {
                _authState.value = AuthState.Error("Sign-out failed")
            }
        }
    }
}
