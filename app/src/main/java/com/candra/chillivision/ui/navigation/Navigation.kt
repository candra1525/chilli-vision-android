package com.candra.chillivision.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.candra.chillivision.ui.pages.history.HistoryScreen
import com.candra.chillivision.ui.pages.login.LoginScreen
import com.candra.chillivision.ui.pages.profile.ProfileScreen
import com.candra.chillivision.ui.pages.register.RegisterScreen
import com.candra.chillivision.ui.pages.scan.ScanScreen

// Mengatur Semua Navigasi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Login.route) {
            LoginScreen()
        }
        composable(route = Screen.Register.route) {
            RegisterScreen()
        }
        composable(route = Screen.Scan.route) {
            ScanScreen()
        }
        composable(route = Screen.History.route) {
            HistoryScreen()
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen()
        }
    }
}