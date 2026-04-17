package dk.itu.moapd.x9.mnla_nals.ViewModels

import android.content.Context
import android.util.Log
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.*
import com.google.firebase.Firebase
import com.google.firebase.auth.*
import dk.itu.moapd.x9.mnla_nals.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class AuthViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val _user = MutableStateFlow(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user.asStateFlow()

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false) // Show all accounts
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val credentialManager = CredentialManager.create(context)
                val result = credentialManager.getCredential(context, request)

                handleSignIn(result.credential)
            } catch (e: CancellationException) {
                Log.e("Auth", "Sign-in cancelled", e)
            }catch (e: GetCredentialCancellationException) {
                Log.e("Auth", "Sign-in cancelled", e)
            }catch (e: GetCredentialException) {
                Log.e("Auth", "Credential Manager error", e)
            }catch (e: NoCredentialException) {
                Log.e("Auth", "No credential found", e)
            }catch (e: Exception) {
                Log.e("Auth", "Sign-in failed", e)
            }
        }
    }

    fun signInAsGuest() {
        // Await
        viewModelScope.launch {
            try {
                // Wait for request to firebase to resolve before assigning user value
                auth.signInAnonymously().await()
                _user.value = auth.currentUser
                Log.d("Auth", "Guest sign-in success: ${auth.currentUser?.uid}")
            }catch (e: CancellationException) {
                Log.e("Auth", "Sign-in cancelled", e)
            } catch (e: FirebaseAuthException) {
                Log.e("Auth", "Sign-in failed", e)
            } catch (e: Exception) {
                Log.e("Auth", "Guest sign-in failed", e)
                _user.value = null
            }
        }
    }

    private suspend fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleIdToken = GoogleIdTokenCredential
                .createFrom(credential.data).idToken

            val firebaseCredential = GoogleAuthProvider
                .getCredential(googleIdToken, null)

            auth.signInWithCredential(firebaseCredential).await()
            _user.value = auth.currentUser
        }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }
}