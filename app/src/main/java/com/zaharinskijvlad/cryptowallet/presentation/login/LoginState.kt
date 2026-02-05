package com.zaharinskijvlad.cryptowallet.presentation.login

data class LoginState(
    val email: String = "",
    val otpCode: String = "",
    val isLoading: Boolean = false,
    val isOtpSent: Boolean = false,
    val isSuccessfullyAuthenticated: Boolean = false,
    val error: String? = null
)