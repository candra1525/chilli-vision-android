package com.candra.chillivision.ui.navigation


// Mengatur Semua Screen yang bisa digunakan
sealed class Screen(val route : String)
{
    data object Home : Screen("home")
    data object Langganan : Screen("langganan")
    data object Scan : Screen("scan")
    data object Profile : Screen("profile")
    data object History : Screen("history")

    // Login
    data object Login : Screen("login")

    // Register
    data object Register : Screen("register")

    // Welcome
    data object Welcome : Screen("welcome")
}