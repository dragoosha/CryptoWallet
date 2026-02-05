package com.zaharinskijvlad.cryptowallet.domain.repository


interface WalletRepository {
    fun getWalletAddress(): String
    suspend fun getBalance(): String
    suspend fun getNetworkName(): String
}