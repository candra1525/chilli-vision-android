package com.candra.chillivision.ui.pages.home.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.Loading
import com.candra.chillivision.component.NotFound
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.convertIsoToDateTime
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.notification.NotificationAll
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.navigation.Screen
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: NotificationScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val notificationState by viewModel.notification.collectAsState()

    var shouldRefresh by remember { mutableStateOf(false) }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.getAllNotification()
            shouldRefresh = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextBold(
                        "Notifikasi",
                        colors = PrimaryGreen,
                        sized = 18
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryGreen
                        )
                    }
                },

                modifier = Modifier.shadow(1.dp), // Shadow manual
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                    scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
                )
            )
        },
        containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            if (shouldRefresh) {
                Loading()
            } else {
                when (notificationState) {
                    is Result.Loading -> {
                        Loading()
                    }

                    is Result.Success -> {
                        val notif =
                            (notificationState as Result.Success).data.data ?: emptyList()
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                PullToRefreshBox(isRefreshing = shouldRefresh, onRefresh = {
                                    shouldRefresh = true
                                }) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(0.dp, 8.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        items(notif) { notification ->
                                            NotificationItem(
                                                notification = notification,
                                                navController = navController
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is Result.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                NotFound(modifier = Modifier.size(200.dp))
                                TextBold(
                                    text = "Tidak ada notifikasi saat ini 😉",
                                    sized = 14,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationItem(
    notification: NotificationAll?,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                navController.navigate(
                    route = Screen.NotificationDetail.route
                        .replace("{title}", notification?.title ?: "-")
                        .replace("{description}", notification?.description ?: "-")
                        .replace("{datePublish}", notification?.createdAt ?: "-")
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextBold(
                    text = notification?.title ?: "-",
                    colors = PrimaryGreen,
                    sized = 16,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(0.dp, 8.dp)
                )
                Text(
                    text = notification?.description ?: "-",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Justify,
                        fontSize = 10.sp,
                        color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                        fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    ),
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                convertIsoToDateTime(notification?.createdAt ?: "-")?.let {
                    TextBold(
                        text = it,
                        sized = 11,
                        textAlign = TextAlign.End,
                        colors = if (isSystemInDarkTheme()) White else BlackMode
                    )
                }
            }
        }
    }
}