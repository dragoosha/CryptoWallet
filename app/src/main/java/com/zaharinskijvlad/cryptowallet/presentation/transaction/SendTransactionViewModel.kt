package com.zaharinskijvlad.cryptowallet.presentation.transaction

import androidx.lifecycle.viewModelScope
import com.zaharinskijvlad.cryptowallet.domain.usecase.SendTransactionUseCase
import com.zaharinskijvlad.cryptowallet.presentation.utils.StatefulViewModel
import kotlinx.coroutines.launch

class SendTransactionViewModel(
    private val sendTransactionUseCase: SendTransactionUseCase
) : StatefulViewModel<SendTransactionState>(SendTransactionState()) {

    fun onRecipientChange(value: String) {
        updateState { copy(recipientAddress = value, error = null) }
    }

    fun onAmountChange(value: String) {
        if (value.all { it.isDigit() || it == '.' }) {
            updateState { copy(amount = value, error = null) }
        }
    }

    fun sendTransaction() {
        val recipient = state.recipientAddress.trim()
        val amount = state.amount.trim()

        if (recipient.length < 40 || !recipient.startsWith("0x")) {
            updateState { copy(error = "Invalid recipient address (must start with 0x)") }
            return
        }
        if (amount.isBlank() || amount.toDoubleOrNull() == 0.0) {
            updateState { copy(error = "Enter a valid amount") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null, transactionHash = null) }
            try {
                val txHash = sendTransactionUseCase(recipient, amount)
                updateState { copy(transactionHash = txHash, isLoading = false) }
            } catch (e: Exception) {
                updateState { copy(error = e.message ?: "Transaction failed", isLoading = false) }
            }
        }
    }

    fun clearState() {
        updateState { SendTransactionState() }
    }
}