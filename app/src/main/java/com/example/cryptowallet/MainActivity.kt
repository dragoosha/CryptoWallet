package com.example.cryptowallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.UI.DynamicUI
import com.dynamic.sdk.android.core.ClientProps
import com.dynamic.sdk.android.core.LoggerLevel
import com.example.cryptowallet.ui.theme.CryptoWalletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val props = ClientProps(
            environmentId = "67b89c58-9693-4c70-bb09-8e373f29b5ef", // Твой ID из консоли
            appName = "Crypto Wallet",
            redirectUrl = "cryptowallet://", // Должно совпадать со схемой в Манифесте
            appOrigin = "https://test.app",
            logLevel = LoggerLevel.DEBUG
        )
        DynamicSDK.initialize(props, applicationContext, this)

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                DynamicUI()
            }
        }
    }
}