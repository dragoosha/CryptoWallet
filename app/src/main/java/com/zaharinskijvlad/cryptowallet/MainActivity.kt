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
import androidx.compose.ui.Alignment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zaharinskijvlad.cryptowallet.presentation.login.LoginScreen
import com.zaharinskijvlad.cryptowallet.presentation.navigation.Screen
import com.zaharinskijvlad.cryptowallet.presentation.wallet.WalletScreen

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
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate(Screen.Wallet.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Wallet.route) {
                        WalletScreen(
                            onNavigateToSend = {
                                navController.navigate(Screen.Send.route)
                            },
                            onLogout = {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Send.route) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        }
                    }
                }

                DynamicUI()
            }
        }
    }
}