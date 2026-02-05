package com.zaharinskijvlad.cryptowallet.data.repository

import android.util.Log
import com.dynamic.sdk.android.DynamicSDK
import com.zaharinskijvlad.cryptowallet.domain.repository.WalletRepository

class WalletRepositoryImpl : WalletRepository {

    private val sdk = DynamicSDK.getInstance()
    private val SEPOLIA_CHAIN_ID = "11155111"
    private fun getEvmWallet() = sdk.wallets.userWallets
        .firstOrNull { it.chain.uppercase() == "EVM" }

    override fun getWalletAddress(): String {
        return getEvmWallet()?.address ?: "Unknown Address"
    }

    override suspend fun getBalance(): String {
        val wallet = getEvmWallet() ?: return "0.00"
        return try {
            sdk.wallets.getBalance(wallet)?.toString() ?: "0.00"
        } catch (e: Exception) {
            "Error"
        }
    }

    override suspend fun getNetworkName(): String {
        val wallet = getEvmWallet() ?: return "No Wallet"

        val chain = wallet.chain

        Log.d(TAG, "chain get from wallet $chain")

        return if (chain == SEPOLIA_CHAIN_ID) {
            "Sepolia"
        } else {
            "Unknown Network ($chain)"
        }
    }

    companion object {
        private const val TAG = "WalletRep"
    }
}