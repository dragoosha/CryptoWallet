package com.zaharinskijvlad.cryptowallet.di

import com.zaharinskijvlad.cryptowallet.data.repository.AuthRepositoryImpl
import com.zaharinskijvlad.cryptowallet.domain.repository.AuthRepository
import com.zaharinskijvlad.cryptowallet.domain.usecase.GetAuthStateUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.LogoutUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.SendOtpUseCase
import com.zaharinskijvlad.cryptowallet.domain.usecase.VerifyOtpUseCase
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }

    factory { SendOtpUseCase(get()) }
    factory { VerifyOtpUseCase(get()) }
    factory { GetAuthStateUseCase(get()) }
    factory { LogoutUseCase(get()) }

}