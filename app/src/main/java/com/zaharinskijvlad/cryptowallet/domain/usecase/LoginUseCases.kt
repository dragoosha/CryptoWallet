package com.zaharinskijvlad.cryptowallet.domain.usecase

import com.zaharinskijvlad.cryptowallet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SendOtpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String) = repository.sendOtp(email)
}

class VerifyOtpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(code: String) = repository.verifyOtp(code)
}

class GetAuthStateUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<Boolean> = repository.getAuthState()
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}