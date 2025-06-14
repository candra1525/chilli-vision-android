package com.candra.chillivision.ui.pages.langganan

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.component.ButtonBorderGreen
import com.candra.chillivision.component.ButtonGreen
import com.candra.chillivision.component.Loading
import com.candra.chillivision.component.NotFound
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.formatRupiah
import com.candra.chillivision.component.konversiFormatTanggal
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.ListHistorySubscription
import com.candra.chillivision.data.response.ListHistorySubscriptionActive
import com.candra.chillivision.data.response.ListHistorySubscriptionActiveResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetAll
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.navigation.Screen
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LanggananScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: LanggananScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    Column(
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp)
    ) {
        LanggananTitle(modifier)
        Spacer(modifier = Modifier.height(16.dp))
        TabScreen(viewModel, navController)
        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Composable
private fun LanggananTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextBold(text = "Langganan", colors = PrimaryGreen, sized = 18)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabScreen(viewModel: LanggananScreenViewModel, navController: NavController) {
    var state by rememberSaveable { mutableIntStateOf(0) }
    val titles = listOf("Beli", "Aktif", "Riwayat")
    val icons =
        listOf(Icons.Default.ShoppingCart, Icons.Default.Star, Icons.AutoMirrored.Filled.List)
    var idUser by remember { mutableStateOf("") }

    var savedSubscriptionsActive by remember {
        mutableStateOf<Result<ListHistorySubscriptionActiveResponse>?>(null)
    }
    var savedSubscriptionsHistory by remember {
        mutableStateOf<Result<ListHistorySubscriptionResponse>?>(null)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(idUser) {
        if (idUser.isEmpty()) {
            viewModel.getPreferences().asLiveData().observe(lifecycleOwner) {
                idUser = it.id
            }
        }
    }

    val scrollStates = remember { mutableStateMapOf(0 to 0, 1 to 0) }


    Column {
        androidx.compose.material3.TabRow(selectedTabIndex = state,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[state]), color = PrimaryGreen
                )
            }) {
            titles.forEachIndexed { index, title ->
                Tab(selected = state == index, onClick = {
                    scrollStates[state] = scrollStates[state] ?: 0
                    state = index
                }) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = title,
                            tint = if (state == index) PrimaryGreen else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextBold(
                            text = title,
                            colors = if (state == index) PrimaryGreen else Color.Gray,
                            sized = 12,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

//        Spacer(modifier = Modifier.height(8.dp))

        when (state) {
            0 -> BeliLanggananScreen(viewModel, navController)

            1 -> {
                if (idUser.isNotEmpty()) {
                    ActiveSubscription(
                        viewModel = viewModel,
                        initialScroll = scrollStates[state] ?: 0,
                        onScrollChanged = { scrollStates[state] = it },
                        idUser = idUser,
                        savedSubscriptionsActive = savedSubscriptionsActive,
                        onSaveSubscriptionsActive = { savedSubscriptionsActive = it },
                        navController = navController
                    )
                }
            }


            2 -> {
                if (idUser.isNotEmpty()) {
                    HistorySubscription(
                        viewModel = viewModel,
                        initialScroll = scrollStates[state] ?: 0,
                        onScrollChanged = { scrollStates[state] = it },
                        idUser = idUser,
                        savedSubscriptionsHistory = savedSubscriptionsHistory,
                        onSaveSubscriptionsHistory = { savedSubscriptionsHistory = it },
                        navController = navController
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeliLanggananScreen(
    viewModel: LanggananScreenViewModel, navController: NavController
) {
    val subscriptionsState by viewModel.subscriptions.collectAsState()
    var shouldRefresh by remember { mutableStateOf(false) }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.fetchSubscriptions()
            shouldRefresh = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (subscriptionsState) {
            is Result.Loading -> {
                Loading()
            }

            is Result.Success -> {
                val subscriptions = (subscriptionsState as Result.Success).data.data ?: emptyList()

                PullToRefreshBox(isRefreshing = shouldRefresh, onRefresh = {
                    shouldRefresh = true
                }) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(0.dp, 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            if (subscriptions.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(LocalConfiguration.current.screenHeightDp.dp - 250.dp)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        NotFound(modifier = Modifier.size(200.dp))
                                        TextBold(
                                            text = "Langganan belum tersedia !",
                                            sized = 14,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        items(subscriptions) { subscription ->
                            SubscriptionCard(subscription, navController)
                        }

                    }
                }
            }

            is Result.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NotFound(modifier = Modifier.size(200.dp))
                        TextBold(
                            text = "Halaman bermasalah 🥲", sized = 14, textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        ButtonGreen(
                            onClick = {
                                shouldRefresh = true
                            },
                            text = "Perbarui",
                            isLoading = false,
                            modifier = Modifier
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(subscription: SubscriptionsGetAll, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = subscription.urlImage ?: "",
                    contentDescription = "Subscription Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextBold(text = (if (subscription.title != null) {
                        subscription.title + subscription.period?.let { " - $it Bulan" }
                    } else {
                        "-"
                    }),
                        colors = PrimaryGreen,
                        sized = 16,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(0.dp, 8.dp))

                    TextBold(
                        text = "${subscription.price?.let { formatRupiah(it) }}",
                        sized = 12,
                        textAlign = TextAlign.End,
                        colors = if (isSystemInDarkTheme()) White else BlackMode
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    ButtonGreen(onClick = {
                        navController.navigate("detailLangganan?idLangganan=${subscription.id}")
                        subscription.id?.let { Log.d("id Subs", it) }
                    }, text = "Lihat Detail", isLoading = false)
                }

            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActiveSubscription(
    viewModel: LanggananScreenViewModel,
    initialScroll: Int,
    onScrollChanged: (Int) -> Unit,
    savedSubscriptionsActive: Result<ListHistorySubscriptionActiveResponse>?,
    idUser: String,
    onSaveSubscriptionsActive: (Result<ListHistorySubscriptionActiveResponse>) -> Unit,
    navController: NavController
) {
    val listState = rememberLazyListState()
    val subscriptionsActiveState by viewModel.subscriptionsActive.collectAsState()
    var shouldRefresh by rememberSaveable { mutableStateOf(false) }
    var isFetched by rememberSaveable { mutableStateOf(false) } // Untuk mencegah fetch ulang saat rotate

    LaunchedEffect(idUser) {
        if (!isFetched && savedSubscriptionsActive == null && idUser.isNotEmpty()) {
            viewModel.fetchSubscriptionsActive(idUser)
            isFetched = true
        }
    }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.fetchSubscriptionsActive(idUser)
            onSaveSubscriptionsActive(subscriptionsActiveState)
            shouldRefresh = false
        }
    }

    LaunchedEffect(subscriptionsActiveState) {
        if (subscriptionsActiveState is Result.Success) {
            onSaveSubscriptionsActive(subscriptionsActiveState)
        }
    }

    val displayedSubscriptionsState = subscriptionsActiveState.takeIf { it is Result.Loading }
        ?: savedSubscriptionsActive

    Box(modifier = Modifier.fillMaxSize()) {
        when (displayedSubscriptionsState) {
            is Result.Loading -> {
                Loading()
            }

            is Result.Success -> {
                val subscriptions = displayedSubscriptionsState.data.data ?: emptyList()

                PullToRefreshBox(isRefreshing = shouldRefresh, onRefresh = {
                    shouldRefresh = true
                }) {

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            if (subscriptions.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(LocalConfiguration.current.screenHeightDp.dp - 250.dp)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        NotFound(modifier = Modifier.size(200.dp))
                                        TextBold(
                                            text = "Tidak ada langganan yang aktif saat ini !",
                                            sized = 14,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        items(subscriptions) { subscription ->
                            ActiveSubscriptionItem(subscription, navController)
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
                            text = "Tidak ada langganan yang aktif saat ini 😉",
                            sized = 14,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        ButtonGreen(
                            onClick = {
                                shouldRefresh = true
                            },
                            text = "Perbarui",
                            isLoading = false,
                            modifier = Modifier
                        )
                    }
                }
            }

            else -> {}
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActiveSubscriptionItem(
    subscription: ListHistorySubscriptionActive?,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = subscription?.subscriptions?.urlImage ?: "",
                    contentDescription = "Subscription Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextBold(
                        text = subscription?.subscriptions?.title?.let {
                            "$it - ${subscription.subscriptions.period ?: "-"} Bulan"
                        } ?: "-",
                        colors = PrimaryGreen,
                        sized = 16,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )

                    subscription?.subscriptions?.price?.let { formatRupiah(it) }?.let {
                        TextBold(
                            text = it,
                            sized = 12,
                            textAlign = TextAlign.End,
                            colors = if (isSystemInDarkTheme()) White else BlackMode
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                subscription?.endDate?.let { konversiFormatTanggal(it) }?.let {
                    TextBold(
                        text = "Berlaku hingga : $it",
                        sized = 11,
                        textAlign = TextAlign.End,
                        colors = if (isSystemInDarkTheme()) White else BlackMode
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                ButtonBorderGreen(
                    onClick = {
                        navController.navigate(
                            route = Screen.DetailActiveLangganan.route
                                .replace(
                                    "{id}",
                                    subscription?.id ?: "-",
                                ).replace(
                                    "{title}",
                                    subscription?.subscriptions?.title ?: "-",
                                ).replace(
                                    "{price}",
                                    subscription?.subscriptions?.price?.let { formatRupiah(it) }
                                        ?: "-",
                                ).replace(
                                    "{startDate}",
                                    subscription?.startDate ?: "-",
                                ).replace(
                                    "{endDate}",
                                    subscription?.endDate ?: "-",
                                ).replace(
                                    "{description}",
                                    subscription?.subscriptions?.description ?: "-",
                                ).replace(
                                    "{statusTransaction}",
                                    subscription?.status ?: "-",
                                ).replace(
                                    "{paymentMethod}",
                                    subscription?.paymentMethod ?: "-",
                                ).replace(
                                    "{period}",
                                    subscription?.subscriptions?.period?.toString() ?: "-",
                                ).replace(
                                    "{urlImageSubscription}",
                                    URLEncoder.encode(
                                        subscription?.subscriptions?.urlImage ?: "-",
                                        StandardCharsets.UTF_8.toString()
                                    )
                                ).replace(
                                    "{urlImageTransaction}",
                                    URLEncoder.encode(
                                        subscription?.imageUrl ?: "-",
                                        StandardCharsets.UTF_8.toString()
                                    )
                                )
                        )
                    },
                    text = "Lihat Detail",
                    textSize = 10,
                    modifier = Modifier.width(120.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistorySubscription(
    viewModel: LanggananScreenViewModel,
    initialScroll: Int,
    onScrollChanged: (Int) -> Unit,
    savedSubscriptionsHistory: Result<ListHistorySubscriptionResponse>?,
    idUser: String,
    onSaveSubscriptionsHistory: (Result<ListHistorySubscriptionResponse>) -> Unit,
    navController: NavController
) {
    val listState = rememberLazyListState()
    var shouldRefresh by remember { mutableStateOf(false) }
    val subscriptionsHistoryState by viewModel.subscriptionsHistory.collectAsState()

    LaunchedEffect(idUser) {
        if (savedSubscriptionsHistory == null && idUser.isNotEmpty()) {
            viewModel.fetchSubscriptionsHistory(idUser)
        }
    }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.fetchSubscriptionsHistory(idUser)
            onSaveSubscriptionsHistory(subscriptionsHistoryState)
            shouldRefresh = false
        }
    }

    LaunchedEffect(subscriptionsHistoryState) {
        if (subscriptionsHistoryState is Result.Success) {
            onSaveSubscriptionsHistory(subscriptionsHistoryState)
        }
    }


    val displayedSubscriptionsState =
        savedSubscriptionsHistory.takeIf { it is Result.Loading } ?: subscriptionsHistoryState

    Box(modifier = Modifier.fillMaxSize()) {
        when (displayedSubscriptionsState) {
            is Result.Loading -> {
                Loading()
            }

            is Result.Success -> {
                val subscriptions = displayedSubscriptionsState.data.data ?: emptyList()

                PullToRefreshBox(isRefreshing = shouldRefresh, onRefresh = {
                    shouldRefresh = true
                }) {

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            if (subscriptions.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(LocalConfiguration.current.screenHeightDp.dp - 250.dp)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        NotFound(modifier = Modifier.size(200.dp))
                                        TextBold(
                                            text = "Tidak ada riwayat langganan !",
                                            sized = 14,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        items(subscriptions) { subscription ->
                            HistorySubscriptionItem(subscription, navController)
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
                            text = "Tidak ada riwayat langganan 😉",
                            sized = 14,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        ButtonGreen(
                            onClick = {
                                shouldRefresh = true
                            },
                            text = "Perbarui",
                            isLoading = false,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistorySubscriptionItem(subscription: ListHistorySubscription?, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = subscription?.subscriptions?.urlImage ?: "",
                    contentDescription = "Subscription Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextBold(
                        text = "${subscription?.subscriptions?.title ?: "-"} - ${subscription?.subscriptions?.period ?: "-"} Bulan",
                        colors = PrimaryGreen,
                        sized = 16,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )

                    TextBold(
                        text = when (subscription?.status) {
                            "pending" -> "Menunggu Konfirmasi"
                            "success" -> "Berhasil"
                            "expired" -> "Kadaluarsa"
                            "cancel" -> "Dibatalkan"
                            else -> "-"
                        },
                        sized = 12,
                        textAlign = TextAlign.End,
                        colors = if (isSystemInDarkTheme()) White else BlackMode
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        ButtonBorderGreen(
                            onClick = {
                                navController.navigate(
                                    route = Screen.DetailHistoryLangganan.route
                                        .replace(
                                            "{id}",
                                            subscription?.id ?: "-",
                                        ).replace(
                                            "{title}",
                                            subscription?.subscriptions?.title ?: "-",
                                        ).replace(
                                            "{price}",
                                            subscription?.subscriptions?.price?.let {
                                                formatRupiah(
                                                    it
                                                )
                                            }
                                                ?: "-",
                                        ).replace(
                                            "{startDate}",
                                            subscription?.startDate ?: "-",
                                        ).replace(
                                            "{endDate}",
                                            subscription?.endDate ?: "-",
                                        ).replace(
                                            "{description}",
                                            subscription?.subscriptions?.description ?: "-",
                                        ).replace(
                                            "{statusTransaction}",
                                            subscription?.status ?: "-",
                                        ).replace(
                                            "{paymentMethod}",
                                            subscription?.paymentMethod ?: "-",
                                        ).replace(
                                            "{period}",
                                            subscription?.subscriptions?.period?.toString() ?: "-",
                                        ).replace(
                                            "{urlImageSubscription}",
                                            URLEncoder.encode(
                                                subscription?.subscriptions?.urlImage ?: "-",
                                                StandardCharsets.UTF_8.toString()
                                            )

                                        ).replace(
                                            "{urlImageTransaction}",
                                            URLEncoder.encode(
                                                subscription?.imageUrl ?: "-",
                                                StandardCharsets.UTF_8.toString()
                                            )
                                        )
                                )
                            },
                            text = "Lihat Detail",
                            textSize = 10,
                            modifier = Modifier.width(120.dp)
                        )
                    }

                }
            }
        }
    }
}
