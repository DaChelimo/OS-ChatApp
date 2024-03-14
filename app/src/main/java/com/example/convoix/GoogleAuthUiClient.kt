package com.example.convoix

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    val viewModel: ChatViewModel,
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result =try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }
    fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        val user = UserData()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                user.email = task.result.user?.email.toString()
                user.userId = task.result.user?.uid.toString()
                viewModel.addUserDataToFirestore(user)
                viewModel.getFCMToken(user.userId)
                viewModel.getUserData(user.userId)
            }
        return try {
            SignInResult(
                data = user?.run {
                    user
                },
                errmsg = null
            )
        } catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data= null,
                errmsg = e.message
            )
        }
    }
    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val cred = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = cred.googleIdToken
        val googleCred = GoogleAuthProvider.getCredential(googleIdToken,null)
        return try {
            val user = auth.signInWithCredential(googleCred).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        email = email.toString(),
                        userId = uid,
                        username = displayName,
                        ppurl = photoUrl.toString().substring(0,photoUrl.toString().length-6)
                    )
                },
                errmsg = null
            )
        } catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data= null,
                errmsg = e.message
            )
        }
    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            email = email.toString(),
            userId = uid,
            username = displayName,
            ppurl = photoUrl.toString()
        )
    }
    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("646575856548-jcc5g79p5q7ji20mjqftkortlfhrs8e8.apps.googleusercontent.com")
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}