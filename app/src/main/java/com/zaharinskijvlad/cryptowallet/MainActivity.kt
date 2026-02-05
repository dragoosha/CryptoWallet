package com.zaharinskijvlad.cryptowallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.UI.DynamicUI
import com.dynamic.sdk.android.core.ClientProps
import com.dynamic.sdk.android.core.LoggerLevel
import androidx.navigation.compose.rememberNavController
import com.zaharinskijvlad.cryptowallet.presentation.navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val props = ClientProps(
            environmentId = "67b89c58-9693-4c70-bb09-8e373f29b5ef",
            appName = "Crypto Wallet",
            redirectUrl = "cryptowallet://",
            appOrigin = "https://test.app",
            logLevel = LoggerLevel.DEBUG
        )
        DynamicSDK.initialize(props, applicationContext, this)

        setContent {
            val navController = rememberNavController()

            Box(modifier = Modifier.fillMaxSize()) {
                Navigation(navController)
                DynamicUI()
            }
        }
    }
}