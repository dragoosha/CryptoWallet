package com.zaharinskijvlad.cryptowallet.presentation.login

import androidx.lifecycle.viewModelScope
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetAuthStateUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.SendOtpUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.VerifyOtpUseCase
import com.zaharinskijvlad.cryptowallet.presentation.utils.StatefulViewModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase
) : StatefulViewModel<LoginState>(LoginState()) {
    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            getAuthStateUseCase().collect { isAuthenticated ->
                updateState { copy(isSuccessfullyAuthenticated = isAuthenticated) }
            }
        }
    }

    fun onEmailChange(email: String) {
        updateState { copy(email = email, error = null) }
    }

    fun onOtpCodeChange(code: String) {
        updateState { copy(otpCode = code, error = null) }
    }

    fun sendOtp() {
        val email = state.email.trim()

        if (email.isBlank()) {
            updateState { copy(error = "Email cannot be empty") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            try {
                sendOtpUseCase(email)
                updateState { copy(isOtpSent = true) }
            } catch (e: Exception) {
                updateState { copy(error = e.message ?: "Failed to send OTP") }
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }

    fun verifyOtp() {
        val code = state.otpCode.trim()
        if (code.isBlank()) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            try {
                verifyOtpUseCase(code)
            } catch (e: Exception) {
                updateState { copy(error = e.message ?: "Invalid OTP code") }
            } finally {
                updateState { copy(isLoading = false) }
            }
        }
    }

    fun dismissError() {
        updateState { copy(error = null) }
    }
}