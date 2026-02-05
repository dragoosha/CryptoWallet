package com.zaharinskijvlad.cryptowallet.presentation.wallet

import androidx.lifecycle.viewModelScope
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetNetworkUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetWalletAddressUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetWalletBalanceUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.LogoutUseCase
import com.zaharinskijvlad.cryptowallet.presentation.utils.StatefulViewModel
import kotlinx.coroutines.launch

class WalletViewModel(
    private val getWalletAddressUseCase: GetWalletAddressUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    private val getNetworkUseCase: GetNetworkUseCase,
    private val logoutUseCase: LogoutUseCase
) : StatefulViewModel<WalletState>(WalletState()) {

    init {
        loadWalletData()
    }

    fun loadWalletData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            try {
                val address = getWalletAddressUseCase()
                val network = getNetworkUseCase()
                val balance = getWalletBalanceUseCase()

                updateState {
                    copy(
                        address = address,
                        networkName = network,
                        balance = balance,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onLogoutComplete()
        }
    }
}