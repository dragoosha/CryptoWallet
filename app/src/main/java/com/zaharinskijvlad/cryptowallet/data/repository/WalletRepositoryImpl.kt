package com.zaharinskijvlad.cryptowallet.data.repository

import android.util.Log
import com.dynamic.sdk.android.Chains.EVM.EthereumTransaction
import com.dynamic.sdk.android.DynamicSDK
import com.zaharinskijvlad.cryptowallet.domain.repository.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger

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

    override suspend fun sendTransaction(toAddress: String, amountEth: String): String {
        val wallet = getEvmWallet() ?: throw Exception("No EVM wallet found")

        return withContext(Dispatchers.IO) {
            val amountWei = try {
                BigDecimal(amountEth).multiply(BigDecimal.TEN.pow(18)).toBigInteger()
            } catch (e: Exception) {
                throw Exception("Invalid amount format")
            }

            val gasLimit = BigInteger.valueOf(21000)
            val maxFeePerGas = BigInteger.valueOf(50_000_000_000L)
            val maxPriorityFeePerGas = BigInteger.valueOf(2_000_000_000L)

            val transaction = EthereumTransaction(
                from = wallet.address,
                to = toAddress,
                value = amountWei,
                gas = gasLimit,
                maxFeePerGas = maxFeePerGas,
                maxPriorityFeePerGas = maxPriorityFeePerGas,
                data = ""
            )

            sdk.evm.sendTransaction(transaction, wallet)
        }
    }
    companion object {
        private const val TAG = "WalletRep"
    }
}