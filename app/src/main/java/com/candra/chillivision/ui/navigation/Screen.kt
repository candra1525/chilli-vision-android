package com.candra.chillivision.ui.navigation


// Mengatur Semua Screen yang bisa digunakan
sealed class Screen(val route: String) {
    // Home
    data object Home : Screen("home")

    // Notification
    data object Notification : Screen("notification")

    // Detail notification
    data object NotificationDetail : Screen("notificationDetail/{title}/{description}/{datePublish}")

    // Langganan
    data object Langganan : Screen("langganan")

    // Detail Langganan
    data object DetailLangganan : Screen("detailLangganan?idLangganan={idLangganan}")

    // Detail Active Langganan
    data object DetailActiveLangganan :
        Screen("detailActiveLangganan/{id}/{title}/{price}/{startDate}/{endDate}/{description}/{statusTransaction}/{paymentMethod}/{period}/{urlImageSubscription}/{urlImageTransaction}")

    // Detail History Langganan
    data object DetailHistoryLangganan : Screen("detailHistoryLangganan/{id}/{title}/{price}/{startDate}/{endDate}/{description}/{statusTransaction}/{period}/{paymentMethod}/{urlImageSubscription}/{urlImageTransaction}")

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
    data object ConfirmScan : Screen("confirmScan?imageUri={imageUri}")

    // Analysis
    data object AnalysisResult : Screen("analysisResult")

    // Detail History
    data object DetailHistory :
        Screen("detailHistory/{idHistory}")

    // Error
    data object Error : Screen("error")

}