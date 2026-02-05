package com.zaharinskijvlad.cryptowallet

import android.app.Application
import com.zaharinskijvlad.cryptowallet.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CryptoApp)
            modules(appModule)
        }
    }
}