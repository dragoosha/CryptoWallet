package com.zaharinskijvlad.cryptowallet.di

import com.zaharinskijvlad.cryptowallet.data.repository.AuthRepositoryImpl
import com.zaharinskijvlad.cryptowallet.data.repository.WalletRepositoryImpl
import com.zaharinskijvlad.cryptowallet.domain.repository.AuthRepository
import com.zaharinskijvlad.cryptowallet.domain.repository.WalletRepository
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetAuthStateUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetNetworkUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetWalletAddressUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetWalletBalanceUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.LogoutUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.SendOtpUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.SendTransactionUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.VerifyOtpUseCase
import com.zaharinskijvlad.cryptowallet.presentation.login.LoginViewModel
import com.zaharinskijvlad.cryptowallet.presentation.transaction.SendTransactionViewModel
import com.zaharinskijvlad.cryptowallet.presentation.wallet.WalletViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }

    factory { SendOtpUseCase(get()) }
    factory { VerifyOtpUseCase(get()) }
    factory { GetAuthStateUseCase(get()) }
    factory { LogoutUseCase(get()) }

    viewModel { LoginViewModel(get(), get(), get()) }

    single<WalletRepository> { WalletRepositoryImpl() }

    factory { GetWalletAddressUseCase(get()) }
    factory { GetWalletBalanceUseCase(get()) }
    factory { GetNetworkUseCase(get()) }

    viewModel { WalletViewModel(get(), get(), get(), get()) }

    factory { SendTransactionUseCase(get()) }

    viewModel { SendTransactionViewModel(get()) }
}