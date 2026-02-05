package com.zaharinskijvlad.cryptowallet.presentation.transaction

data class SendTransactionState(
    val recipientAddress: String = "",
    val amount: String = "",
    val isLoading: Boolean = false,
    val transactionHash: String? = null,
    val error: String? = null
)