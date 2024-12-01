package com.candra.chillivision.ui.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.candra.chillivision.ui.pages.history.HistoryScreen
import com.candra.chillivision.ui.pages.home.HomeScreen
import com.candra.chillivision.ui.pages.login.LoginScreen
import com.candra.chillivision.ui.pages.profile.LanggananScreen
import com.candra.chillivision.ui.pages.profile.ProfileScreen
import com.candra.chillivision.ui.pages.register.RegisterScreen
import com.candra.chillivision.ui.pages.scan.ScanScreen
import com.candra.chillivision.ui.pages.welcome.WelcomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Mengatur Semua Navigasi
@OptIn(ExperimentalAnimationApi::class, DelicateCoroutinesApi::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier,
         bottomBar = {
             if (currentRoute != Screen.Login.route) {
                 BottomNavigation(navController = navController)
             }
         }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding))
        Box(
            modifier = Modifier
                .fillMaxSize() // Pastikan elemen konten menyesuaikan dengan ukuran
        ) {
            BackHandler {
                if (currentRoute == Screen.Home.route) {
                    GlobalScope.launch {
                        delay(200)
                        (context as? ComponentActivity)?.finish()
                    }
                } else {
                    navController.popBackStack()
                }
            }

            // Konten lainnya di sini

        }

        AnimatedNavHost(
            navController = navController,
            startDestination = Screen.Home.route,
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
                ScanScreen(modifier)
            }
            composable(route = Screen.History.route) {
                HistoryScreen(modifier)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(modifier)
            }
            composable(route = Screen.Welcome.route) {
                WelcomeScreen(modifier, navController)
            }
            composable(route = Screen.Home.route) {
                HomeScreen(modifier, navController)
            }
            composable(route = Screen.Langganan.route) {
                LanggananScreen(modifier)
            }
        }
    }
}

