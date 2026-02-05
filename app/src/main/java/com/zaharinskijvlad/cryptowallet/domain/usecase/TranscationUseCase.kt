package com.zaharinskijvlad.cryptowallet.domain.usecase

import com.zaharinskijvlad.cryptowallet.domain.repository.WalletRepository

class SendTransactionUseCase(private val repository: WalletRepository) {
    suspend operator fun invoke(toAddress: String, amountEth: String): String {
        return repository.sendTransaction(toAddress, amountEth)
    }
}