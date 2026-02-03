package com.example.cryptowallet.data.repository

import com.dynamic.sdk.android.DynamicSDK
import com.example.cryptowallet.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl : AuthRepository {

    private val sdk = DynamicSDK.getInstance()

    override suspend fun sendOtp(email: String) {
        sdk.auth.email.sendOTP(email)
    }

    override suspend fun verifyOtp(code: String) {
        sdk.auth.email.verifyOTP(code)
    }

    override fun getAuthState(): Flow<Boolean> {
        return sdk.auth.tokenChanges.map { token ->
            !token.isNullOrEmpty()
        }
    }

    override suspend fun logout() {
        sdk.auth.logout()
    }
}