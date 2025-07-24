package com.example.elearningplatform.ui.auth

import androidx.lifecycle.*
import com.example.elearningplatform.data.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _state = MutableLiveData<Result<FirebaseUser?>>()
    val state: LiveData<Result<FirebaseUser?>> = _state

    fun login(email: String, password: String) {
        _state.value = Result.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { _state.value = Result.Success(it.user) }
            .addOnFailureListener { _state.value = Result.Error(it) }
    }

    fun register(email: String, password: String, name: String) {
        _state.value = Result.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { res ->
                res.user?.let { user ->
                    db.collection("users").document(user.uid).set(
                        mapOf(
                            "displayName" to name,
                            "email" to email,
                            "createdAt" to FieldValue.serverTimestamp()
                        )
                    )
                }
                _state.value = Result.Success(res.user)
            }
            .addOnFailureListener { _state.value = Result.Error(it) }
    }
}
