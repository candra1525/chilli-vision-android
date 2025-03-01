package com.candra.chillivision.ui.navigation


// Mengatur Semua Screen yang bisa digunakan
sealed class Screen(val route : String)
{
    // Home
    data object Home : Screen("home")

    // Langganan
    data object Langganan : Screen("langganan")

    // Detail Langganan
    data object DetailLangganan : Screen("detailLangganan")

    // Scan
    data object Scan : Screen("scan")

    // Profil
    data object Profile : Screen("profile")

    // History
    data object History : Screen("history")

    // Login
    data object Login : Screen("login")

    // Register
    data object Register : Screen("register")

    // Welcome
    data object Welcome : Screen("welcome")

    // Ubah Profile
    data object ChangeProfile : Screen("changeProfile")

    // Ubah Password
    data object ChangePassword : Screen("changePassword")

    // About Apps
    data object AboutApps : Screen("aboutApps")

    // Tanya AI
    data object TanyaAI : Screen("chilliAI")

    // Gallery
    //    data object Gallery : Screen("gallery")

    // Terms
    data object Terms : Screen("terms")

    // Privacy
    data object Privacy : Screen("privacy")

    // Confirm Screen
    data object ConfirmScan : Screen("confirmScan")

    // Analysis
    data object Analysis : Screen("analysis")

    // Detail History
    data object DetailHistory : Screen("detailHistory")

    // Error
    data object Error : Screen("error")

}