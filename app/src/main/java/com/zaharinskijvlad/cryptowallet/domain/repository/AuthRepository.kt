package com.zaharinskijvlad.cryptowallet.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendOtp(email: String)
    suspend fun verifyOtp(code: String)
    fun getAuthState(): Flow<Boolean>
    suspend fun logout()
}