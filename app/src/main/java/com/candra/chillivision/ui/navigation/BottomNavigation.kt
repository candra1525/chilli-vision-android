package com.candra.chillivision.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.candra.chillivision.R
import com.candra.chillivision.ui.theme.GreenSoft
import com.candra.chillivision.ui.theme.PrimaryGreen

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navStackBackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navStackBackEntry?.destination?.route

        val navItems = listOf(
            NavigationItem(
                title = "Beranda",
                icon = painterResource(id = R.drawable.home),
                screen = Screen.Home,
                contentDescription = "home"
            ),
            NavigationItem(
                title = "Langganan",
                icon = painterResource(id = R.drawable.subscription),
                screen = Screen.Langganan,
                contentDescription = "langganan"
            ),
            NavigationItem(
                title = "Pindai",
                icon = painterResource(id = R.drawable.scan),
                screen = Screen.Scan,
                contentDescription = "pindai"
            ),
            NavigationItem(
                title = "Riwayat",
                icon = painterResource(id = R.drawable.history),
                screen = Screen.History,
                contentDescription = "riwayat"
            ),
            NavigationItem(
                title = "Pengaturan",
                icon = painterResource(id = R.drawable.profile),
                screen = Screen.Profile,
                contentDescription = "profil"
            )
        )

        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(20.dp),
                        tint = if (currentRoute == item.screen.route) PrimaryGreen else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                            color = if (currentRoute == item.screen.route) PrimaryGreen else MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = GreenSoft // Background hijau untuk item yang dipilih
                )
            )
        }
    }
}
