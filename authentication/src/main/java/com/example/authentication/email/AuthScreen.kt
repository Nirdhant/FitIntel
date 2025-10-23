package com.example.authentication.email

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authentication.R

@Composable
fun AuthScreen(context:Context,viewModel: AuthViewModel , onAuthSuccess: () -> Unit) {
    val authState by viewModel.authState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize(context)
    }
    LaunchedEffect(authState) {                        //i can combine  both of these
        when (authState){
            is AuthState.Authenticated -> onAuthSuccess()
            else -> {}
        }
    }
    when (authState) {
        is AuthState.Initial , AuthState.NotAuthenticated -> {
            SignInContent(
                onGoogleSignInClick = { viewModel.signInWithGoogle(context) }       //why using context here
            )
        }
        is AuthState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.padding(top=120.dp, start = 120.dp).size(80.dp))
        }
        is AuthState.Authenticated -> {
            userProfile?.let { profile ->
//                ProfileContent(
//                    profile = profile,
//                    onSignOutClick = { viewModel.signOut(context) }
//                )
                onAuthSuccess()
                Toast.makeText(context,"Home Screen ${profile.displayName}, ${profile.email}",Toast.LENGTH_LONG).show()

            }
        }
        is AuthState.Error -> {
//            ErrorContent(
//                message = authState.message,
//                onRetryClick = { viewModel.signInWithGoogle(context) }
//            )
            Toast.makeText(context, (authState as AuthState.Error).message,Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
private fun SignInContent(
    onGoogleSignInClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onGoogleSignInClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Sign in with Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

