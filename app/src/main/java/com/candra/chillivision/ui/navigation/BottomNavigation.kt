package com.candra.chillivision.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.candra.chillivision.ui.theme.PrimaryGreen

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    NavigationBar(
        modifier = Modifier
            .height(80.dp)
    ) {
        val navStackBackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navStackBackEntry?.destination?.route

        val navItem = listOf(
            NavigationItem(
                title = "Beranda",
                icon = painterResource(id = R.drawable.home),
                screen = Screen.Home,
                contentDescription = "home",

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
                title = "Profil",
                icon = painterResource(id = R.drawable.profile),
                screen = Screen.Profile,
                contentDescription = "profil"
            ),
        )

        navItem.map { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                modifier = Modifier.padding(top = 16.dp),
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
                    if (currentRoute == item.screen.route) {
                        when ((item.screen.route)) {
                            Screen.Home.route -> {
                                Icon(
                                    painter = painterResource(R.drawable.home),
                                    contentDescription = item.title,
                                    modifier = modifier.size(18.dp),
                                    tint = PrimaryGreen
                                )
                            }

                            Screen.Langganan.route -> {
                                Icon(
                                    painter = painterResource(R.drawable.subscription),
                                    contentDescription = item.title,
                                    modifier = modifier.size(18.dp),
                                    tint = PrimaryGreen
                                )
                            }

                            Screen.Scan.route -> {
                                Icon(
                                    painter = painterResource(R.drawable.scan),
                                    contentDescription = item.title,
                                    modifier = modifier.size(18.dp),
                                    tint = PrimaryGreen
                                )
                            }

                            Screen.History.route -> {
                                Icon(
                                    painter = painterResource(R.drawable.history),
                                    contentDescription = item.title,
                                    modifier = modifier.size(18.dp),
                                    tint = PrimaryGreen
                                )
                            }

                            Screen.Profile.route -> {
                                Icon(
                                    painter = painterResource(R.drawable.profile),
                                    contentDescription = item.title,
                                    modifier = modifier.size(18.dp),
                                    tint = PrimaryGreen
                                )
                            }

                            else -> {
                                Icon(
                                    painter = item.icon, contentDescription = item.title,
                                    modifier = modifier.size(18.dp),
                                )
                            }
                        }
                    } else {
                        Icon(
                            painter = item.icon, contentDescription = item.title,
                            modifier = modifier.size(18.dp),
                        )
                    }
                },
                label = {
                    if (currentRoute == item.screen.route) {
                        Text(
                            text = item.title, style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 9.sp,
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(
                                    Font(R.font.quicksand_bold)
                                )
                            )
                        )
                    }
                },
                alwaysShowLabel = false,
            )
        }
    }
}