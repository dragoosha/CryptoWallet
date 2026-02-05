package com.zaharinskijvlad.cryptowallet.domain.usecase

import com.zaharinskijvlad.cryptowallet.domain.repository.WalletRepository

class GetWalletAddressUseCase(private val repository: WalletRepository) {
    operator fun invoke(): String = repository.getWalletAddress()
}

class GetWalletBalanceUseCase(private val repository: WalletRepository) {
    suspend operator fun invoke(): String = repository.getBalance()
}

class GetNetworkUseCase(private val repository: WalletRepository) {
    suspend operator fun invoke(): String = repository.getNetworkName()
}