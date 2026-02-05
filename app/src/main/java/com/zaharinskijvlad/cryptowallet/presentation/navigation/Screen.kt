package com.zaharinskijvlad.cryptowallet.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Wallet : Screen("wallet")
    data object Send : Screen("send")
}