package com.zaharinskijvlad.cryptowallet.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zaharinskijvlad.cryptowallet.presentation.login.LoginScreen
import com.zaharinskijvlad.cryptowallet.presentation.transaction.SendTransactionScreen
import com.zaharinskijvlad.cryptowallet.presentation.wallet.WalletScreen
@Composable
fun Navigation (
    navController: NavHostController
){
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
            SendTransactionScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}