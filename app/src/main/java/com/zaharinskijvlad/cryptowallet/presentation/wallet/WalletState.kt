package com.zaharinskijvlad.cryptowallet.presentation.wallet

data class WalletState(
    val address: String = "...",
    val balance: String = "Loading...",
    val networkName: String = "Loading...",
    val isLoading: Boolean = false,
    val error: String? = null
)