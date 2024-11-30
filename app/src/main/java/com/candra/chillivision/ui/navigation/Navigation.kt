package com.candra.chillivision.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.candra.chillivision.ui.pages.history.HistoryScreen
import com.candra.chillivision.ui.pages.home.HomeScreen
import com.candra.chillivision.ui.pages.login.LoginScreen
import com.candra.chillivision.ui.pages.profile.ProfileScreen
import com.candra.chillivision.ui.pages.register.RegisterScreen
import com.candra.chillivision.ui.pages.scan.ScanScreen
import com.candra.chillivision.ui.pages.welcome.WelcomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

// Mengatur Semua Navigasi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(modifier, navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(modifier, navController)
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
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(modifier, navController)
        }
        composable(route = Screen.Home.route) {
             HomeScreen(modifier, navController)
        }
    }
}

