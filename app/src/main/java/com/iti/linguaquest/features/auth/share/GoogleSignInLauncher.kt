package com.iti.linguaquest.features.auth.share

import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.iti.linguaquest.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber


fun ComponentActivity.launchGoogleSignIn(
    onTokenReceived: (String) -> Unit,
    onError: () -> Unit
) {
    lifecycleScope.launch {
        try {
            val credentialManager = CredentialManager.create(this@launchGoogleSignIn.applicationContext)
            val clientIdResId = resources.getIdentifier("default_web_client_id", "string", packageName)
            val clientId = if (clientIdResId != 0) getString(clientIdResId) else "PLEASE_ENABLE_GOOGLE_AUTH_IN_FIREBASE"

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(clientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(this@launchGoogleSignIn, request)
            val credential = result.credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = Firebase.auth.signInWithCredential(firebaseCredential).await()

                val firebaseTokenResult = authResult.user?.getIdToken(false)?.await()
                val firebaseIdToken = firebaseTokenResult?.token

                if (firebaseIdToken != null) {
                    onTokenReceived(firebaseIdToken)
                } else {
                    onError()
                }
            } else {
                onError()
            }
        } catch (e: Exception) {
            Timber.e(e, "Google Sign-In failed")
            onError()
        }
    }
}
