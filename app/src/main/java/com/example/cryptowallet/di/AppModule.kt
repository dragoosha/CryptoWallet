package com.example.cryptowallet.di

import com.example.cryptowallet.data.repository.AuthRepositoryImpl
import com.example.cryptowallet.domain.repository.AuthRepository
import com.example.cryptowallet.domain.usecase.GetAuthStateUseCase
import com.example.cryptowallet.domain.usecase.LogoutUseCase
import com.example.cryptowallet.domain.usecase.SendOtpUseCase
import com.example.cryptowallet.domain.usecase.VerifyOtpUseCase
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }

    factory { SendOtpUseCase(get()) }
    factory { VerifyOtpUseCase(get()) }
    factory { GetAuthStateUseCase(get()) }
    factory { LogoutUseCase(get()) }

}