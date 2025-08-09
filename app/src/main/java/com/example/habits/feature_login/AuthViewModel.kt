package com.example.habits.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habits.core.data.habitcategories.repository.CategoryRepository
import com.example.habits.core.data.repository.UserRepository
import com.example.habits.feature_habits.common.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUser: FirebaseUser? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _uiState.update { it.copy(currentUser = firebaseAuth.currentUser) }
    }

    init {
        auth.addAuthStateListener(authStateListener)
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                authResult.user?.let { firebaseUser ->
                    val newUser = User(
                        uid = firebaseUser.uid,
                        name = name,
                        email = firebaseUser.email ?: ""
                    )
                    val createUserResult = userRepository.createUserDocument(newUser)
                    if (createUserResult.isFailure) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = createUserResult.exceptionOrNull()?.message
                                    ?: "Failed to save user details."
                            )
                        }
                        return@launch
                    }

                    val createCategoriesResult =
                        categoryRepository.createDefaultCategoriesForUser(firebaseUser.uid)
                    if (createCategoriesResult.isFailure) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = createCategoriesResult.exceptionOrNull()?.message
                                    ?: "Failed to set up default categories."
                            )
                        }
                        return@launch
                    }
                }
                // currentUser will be updated by the authStateListener
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }
}
