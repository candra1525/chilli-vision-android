package com.candra.chillivision.ui.navigation

//import com.candra.chillivision.ui.pages.scan.gallery.GalleryScreen
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.candra.chillivision.component.ConnectivityStatus
import com.candra.chillivision.ui.pages.analysis.AnalysisScreen
import com.candra.chillivision.ui.pages.error.ErrorScreen
import com.candra.chillivision.ui.pages.history.HistoryScreen
import com.candra.chillivision.ui.pages.history.detail_history.DetailHistoryScreen
import com.candra.chillivision.ui.pages.home.HomeScreen
import com.candra.chillivision.ui.pages.home.tanyaAI.ChilliAIScreen
import com.candra.chillivision.ui.pages.langganan.LanggananScreen
import com.candra.chillivision.ui.pages.langganan.detail.DetailLanggananScreen
import com.candra.chillivision.ui.pages.langganan.detail_active.DetailActiveLanggananScreen
import com.candra.chillivision.ui.pages.langganan.detail_history.DetailHistoryLanggananScreen
import com.candra.chillivision.ui.pages.login.LoginScreen
import com.candra.chillivision.ui.pages.profile.ProfileScreen
import com.candra.chillivision.ui.pages.profile.lainnya.TentangAplikasi
import com.candra.chillivision.ui.pages.profile.ubah.UbahKataSandi
import com.candra.chillivision.ui.pages.profile.ubah.UbahProfile
import com.candra.chillivision.ui.pages.register.RegisterScreen
import com.candra.chillivision.ui.pages.scan.ScanScreen
import com.candra.chillivision.ui.pages.scan.confirm_scan.ConfirmScanScreen
import com.candra.chillivision.ui.pages.terms_privacy.privacy.PrivacyScreen
import com.candra.chillivision.ui.pages.terms_privacy.terms.TermsScreen
import com.candra.chillivision.ui.pages.welcome.WelcomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// Mengatur Semua Navigasi
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class, DelicateCoroutinesApi::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val connectivityStatus = remember { ConnectivityStatus(context) }
    val isConnected by connectivityStatus.isConnected.observeAsState(initial = true)

    // Daftar rute yang tidak membutuhkan BottomNavigation
    val hideBottomNavRoutes = listOf(
        Screen.Login.route,
        Screen.AboutApps.route,
        Screen.ChangePassword.route,
        Screen.ChangeProfile.route,
        Screen.Welcome.route,
        Screen.Register.route,
        Screen.TanyaAI.route,
//        Screen.Gallery.route,
        Screen.Error.route,
        Screen.Privacy.route,
        Screen.Terms.route,
        Screen.ConfirmScan.route,
        Screen.Analysis.route,
        Screen.DetailLangganan.route,
        Screen.DetailHistory.route,
        Screen.DetailActiveLangganan.route,
        Screen.DetailHistoryLangganan.route
    )

    // Jika tidak ada koneksi, alihkan ke ErrorScreen
    if (!isConnected && currentRoute != Screen.Error.route) {
        if (currentRoute !in hideBottomNavRoutes) {
            BottomNavigation(navController = navController)
        }
        navController.navigate(Screen.Error.route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    Scaffold(modifier = Modifier, bottomBar = {
        if (hideBottomNavRoutes.any { currentRoute?.startsWith(it) == true }.not()) {
            BottomNavigation(navController = navController)
        }
    }) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding))
        Box(
            modifier = Modifier.fillMaxSize() // Pastikan elemen konten menyesuaikan dengan ukuran
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

        AnimatedNavHost(navController = navController,
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
                ScanScreen(modifier, navController)
            }
            composable(route = Screen.History.route) {
                HistoryScreen(modifier, navController)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(modifier, navController)
            }
            composable(route = Screen.Welcome.route) {
                WelcomeScreen(modifier, navController)
            }
            composable(route = Screen.Home.route) {
                HomeScreen(modifier, navController)
            }
            composable(route = Screen.Langganan.route) {
                LanggananScreen(modifier, navController)
            }
            composable(route = Screen.ChangePassword.route) {
                UbahKataSandi(modifier, navController)
            }
            composable(route = Screen.AboutApps.route) {
                TentangAplikasi(modifier, navController)
            }
            composable(route = Screen.ChangeProfile.route) {
                UbahProfile(modifier, navController)
            }
            composable(route = Screen.TanyaAI.route) {
                ChilliAIScreen(modifier, navController)
            }

            composable(Screen.DetailHistory.route) { backStackEntry ->
                val idHistory = backStackEntry.arguments?.getString("idHistory")
                val title = backStackEntry.arguments?.getString("title")
                val description = backStackEntry.arguments?.getString("description")
                val createdAt = backStackEntry.arguments?.getString("createdAt")
                val urlImage = backStackEntry.arguments?.getString("urlImage")?.let {
                    URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                }
                if (idHistory != null && title != null && description != null && createdAt != null && urlImage != null) {
                    DetailHistoryScreen(
                        modifier = modifier,
                        navController = navController,
                        idHistory = idHistory,
                        title = title,
                        description = description,
                        createdAt = createdAt,
                        urlImage = urlImage,
                    )
                }
            }

            composable(route = Screen.Error.route) {
                ErrorScreen(modifier, navController)
            }

            composable(route = Screen.Terms.route) {
                TermsScreen(modifier, navController)
            }

            composable(route = Screen.Privacy.route) {
                PrivacyScreen(modifier, navController)
            }

            composable(Screen.ConfirmScan.route) {
                ConfirmScanScreen(modifier, navController)
            }

            composable(route = Screen.Analysis.route) {
                AnalysisScreen(modifier, navController)
            }

            composable(Screen.DetailLangganan.route) {
                DetailLanggananScreen(modifier, navController)
            }

            composable(Screen.DetailActiveLangganan.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val title = backStackEntry.arguments?.getString("title")
                val price = backStackEntry.arguments?.getString("price")
                val startDate = backStackEntry.arguments?.getString("startDate")
                val endDate = backStackEntry.arguments?.getString("endDate")
                val description = backStackEntry.arguments?.getString("description")
                val statusTransaction = backStackEntry.arguments?.getString("statusTransaction")
                val paymentMethod = backStackEntry.arguments?.getString("paymentMethod")
                val period = backStackEntry.arguments?.getString("period")
                val urlImageSubscription =
                    backStackEntry.arguments?.getString("urlImageSubscription")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    }
                val urlImageTransaction =
                    backStackEntry.arguments?.getString("urlImageTransaction")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    }

                if (
                    id != null &&
                    title != null &&
                    price != null &&
                    startDate != null &&
                    endDate != null &&
                    description != null &&
                    statusTransaction != null &&
                    paymentMethod != null &&
                    period != null &&
                    urlImageSubscription != null &&
                    urlImageTransaction != null
                ) {
                    DetailActiveLanggananScreen(
                        modifier = modifier,
                        navController = navController,
                        id = id,
                        title = title,
                        price = price,
                        startDate = startDate,
                        endDate = endDate,
                        description = description,
                        statusTransaction = statusTransaction,
                        paymentMethod = paymentMethod,
                        period = period,
                        urlImageSubscription = urlImageSubscription,
                        urlImageTransaction = urlImageTransaction
                    )
                }
            }

            composable(Screen.DetailHistoryLangganan.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val title = backStackEntry.arguments?.getString("title")
                val price = backStackEntry.arguments?.getString("price")
                val startDate = backStackEntry.arguments?.getString("startDate")
                val endDate = backStackEntry.arguments?.getString("endDate")
                val description = backStackEntry.arguments?.getString("description")
                val statusTransaction = backStackEntry.arguments?.getString("statusTransaction")
                val paymentMethod = backStackEntry.arguments?.getString("paymentMethod")
                val period = backStackEntry.arguments?.getString("period")
                val urlImageSubscription =
                    backStackEntry.arguments?.getString("urlImageSubscription")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    }
                val urlImageTransaction =
                    backStackEntry.arguments?.getString("urlImageTransaction")?.let {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    }

                if (
                    id != null &&
                    title != null &&
                    price != null &&
                    startDate != null &&
                    endDate != null &&
                    description != null &&
                    statusTransaction != null &&
                    paymentMethod != null &&
                    period != null &&
                    urlImageSubscription != null &&
                    urlImageTransaction != null
                ) {
                    DetailHistoryLanggananScreen(
                        modifier = modifier,
                        navController = navController,
                        id = id,
                        title = title,
                        price = price,
                        startDate = startDate,
                        endDate = endDate,
                        description = description,
                        statusTransaction = statusTransaction,
                        paymentMethod = paymentMethod,
                        period = period,
                        urlImageSubscription = urlImageSubscription,
                        urlImageTransaction = urlImageTransaction
                    )
                }
            }
        }
    }
}

