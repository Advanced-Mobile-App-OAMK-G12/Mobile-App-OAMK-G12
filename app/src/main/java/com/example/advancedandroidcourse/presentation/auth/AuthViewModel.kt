package com.example.advancedandroidcourse.presentation.auth
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }


    fun checkAuthStatus(){
        auth.currentUser?.reload()?.addOnCompleteListener {
            if(auth.currentUser == null){
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.Authenticated
            }
        } ?: run {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email : String,password : String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signup(email : String,password : String, confirmPassword: String, username: String){

        if(email.isEmpty() || password.isEmpty()|| confirmPassword.isEmpty() || username.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match.")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = hashMapOf(
                        "name" to username,
                        "email" to email,
                        "image" to "",
                        "bio" to "",
                        "banStatus" to hashMapOf(
                            "isBanned" to false,
                            "reason" to "",
                            "bannedUntil" to null
                        ),
                        "createdAt" to Timestamp.now(),
                        "lastViewedNotifications" to Timestamp.now()
                    )
                    firestore.collection("users").document(userId)
                        .set(userMap)
                        .addOnSuccessListener {
                            _authState.value = AuthState.Authenticated
                        }
                        .addOnFailureListener { e ->
                            _authState.value = AuthState.Error("Firestore error: ${e.message}")
                        }

                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Signup failed")
                }
            }
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun loginWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser ?: return@addOnCompleteListener
                    val uid = currentUser.uid
                    val userDoc = firestore.collection("users").document(uid)

                    userDoc.get()
                        .addOnSuccessListener { document ->
                            if (!document.exists()) {
                                val userMap = hashMapOf(
                                    "name" to (currentUser.displayName ?: ""),
                                    "email" to (currentUser.email ?: ""),
                                    "image" to (currentUser.photoUrl?.toString() ?: ""),
                                    "bio" to "",
                                    "banStatus" to hashMapOf(
                                        "isBanned" to false,
                                        "reason" to "",
                                        "bannedUntil" to null
                                    ),
                                    "createdAt" to Timestamp.now(),
                                    "lastViewedNotifications" to Timestamp.now()
                                )

                                userDoc.set(userMap)
                                    .addOnSuccessListener {
                                        _authState.value = AuthState.Authenticated
                                    }
                                    .addOnFailureListener { e ->
                                        _authState.value = AuthState.Error("Firestore write error: ${e.message}")
                                    }
                            } else {
                                _authState.value = AuthState.Authenticated
                            }
                        }
                        .addOnFailureListener { e ->
                            _authState.value = AuthState.Error("Firestore fetch error: ${e.message}")
                        }

                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Google login failed")
                }
            }
    }

}


sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

