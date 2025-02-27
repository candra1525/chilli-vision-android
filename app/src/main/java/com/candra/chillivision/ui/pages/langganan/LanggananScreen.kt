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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.ButtonBorderGreen
import com.candra.chillivision.component.ButtonGreen
import com.candra.chillivision.component.NotFound
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.formatRupiah
import com.candra.chillivision.component.konversiFormatTanggal
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.ListHistorySubscriptionActiveResponse
import com.candra.chillivision.data.response.ListHistorySubscriptionResponse
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetAll
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White

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
        modifier = modifier
            .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 90.dp)
    ) {
        LanggananTitle(modifier)
        Spacer(modifier = Modifier.height(16.dp))
        TabScreen(viewModel, navController)
        Spacer(modifier = Modifier.height(16.dp))

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
    LaunchedEffect(Unit) {
        viewModel.getPreferences().asLiveData().observe(lifecycleOwner) {
            idUser = it.id
        }
    }

    val scrollStates = remember { mutableStateMapOf(0 to 0, 1 to 0) }


    Column {
        androidx.compose.material3.TabRow(
            selectedTabIndex = state,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[state]),
                    color = PrimaryGreen
                )
            }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = {
                        scrollStates[state] = scrollStates[state] ?: 0
                        state = index
                    }
                ) {
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

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            0 -> BeliLanggananScreen(viewModel, navController)

            1 -> ActiveSubscription(
                viewModel = viewModel,
                initialScroll = scrollStates[state] ?: 0,
                onScrollChanged = { scrollStates[state] = it },
                idUser = idUser,
                savedSubscriptionsActive = savedSubscriptionsActive,
                onSaveSubscriptionsActive = { savedSubscriptionsActive = it }
            )

            2 -> HistorySubscription(
                viewModel = viewModel,
                initialScroll = scrollStates[state] ?: 0,
                onScrollChanged = { scrollStates[state] = it },
                idUser = idUser,
                savedSubscriptionsHistory = savedSubscriptionsHistory,
                onSaveSubscriptionsHistory = { savedSubscriptionsHistory = it }
            )
        }
    }
}

@Composable
fun BeliLanggananScreen(
    viewModel: LanggananScreenViewModel,
    navController: NavController
) {
    val subscriptionsState by viewModel.subscriptions.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (subscriptionsState) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedLoading(modifier = Modifier.size(120.dp))
                        TextBold(
                            text = "Sedang memuat...",
                            sized = 14,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is Result.Success -> {
                val subscriptions = (subscriptionsState as Result.Success).data.data ?: emptyList()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(0.dp, 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(subscriptions) { subscription ->
                        SubscriptionCard(subscription, navController)
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
                            text = "Halaman bermasalah 🥲",
                            sized = 14,
                            textAlign = TextAlign.Center
                        )
                        // button load ulang
                        ButtonGreen(onClick = {
                            viewModel.fetchSubscriptions()
                        }, text = "Coba Lagi", isLoading = false)

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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
                        .padding(8.dp, 0.dp)
                        .fillMaxWidth()
                ) {
                    TextBold(
                        text = (
                                if (subscription.title != null) {
                                    subscription.title + subscription.period?.let { " - $it Bulan" }
                                } else {
                                    "-"
                                }),
                        colors = PrimaryGreen,
                        sized = 18,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )

                    TextBold(
                        text = "${subscription.price?.let { formatRupiah(it) }}",
                        sized = 16,
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActiveSubscription(
    viewModel: LanggananScreenViewModel,
    initialScroll: Int,
    onScrollChanged: (Int) -> Unit,
    savedSubscriptionsActive: Result<ListHistorySubscriptionActiveResponse>?,
    idUser: String,
    onSaveSubscriptionsActive: (Result<ListHistorySubscriptionActiveResponse>) -> Unit // Tambahkan ini
) {
    val scrollState = rememberScrollState(initialScroll)

    val subscriptionsActiveState by viewModel.subscriptionsActive.collectAsState()

    LaunchedEffect(idUser) {
        if (savedSubscriptionsActive == null) {
            viewModel.fetchSubscriptionsActive(idUser)
        }
    }

    LaunchedEffect(subscriptionsActiveState) {
        if (subscriptionsActiveState is Result.Success) {
            onSaveSubscriptionsActive(subscriptionsActiveState) // Perbarui di TabScreen
        }
    }

    val displayedSubscriptionsState = savedSubscriptionsActive ?: subscriptionsActiveState

    Box(modifier = Modifier.fillMaxSize()) {
        when (displayedSubscriptionsState) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedLoading(modifier = Modifier.size(120.dp))
                        TextBold(
                            text = "Sedang memuat...",
                            sized = 14,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is Result.Success -> {
                val subscriptions =
                    (displayedSubscriptionsState as Result.Success<ListHistorySubscriptionActiveResponse>).data

                if (subscriptions.data?.isEmpty() == true) {
                    Text(
                        text = "Tidak ada langganan aktif",
                        modifier = Modifier,
                        color = Color.Gray
                    )
                } else {
                    subscriptions.data?.forEach { subscription ->
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
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Jika ada gambar di subscription, tampilkan
                                    AsyncImage(
                                        model = subscription?.imageUrl ?: "",
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
                                            .padding(8.dp, 0.dp)
                                            .fillMaxWidth()
                                    ) {
                                        TextBold(
                                            text = (
                                                    if (subscription?.subscriptions?.title != null) {
                                                        subscription.subscriptions.title + subscription.subscriptions.period?.let { " - $it Bulan" }
                                                    } else {
                                                        "-"
                                                    }),
                                            colors = PrimaryGreen,
                                            sized = 18,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.padding(0.dp, 8.dp)
                                        )

                                        subscription?.subscriptions?.price?.let { formatRupiah(it) }
                                            ?.let {
                                                TextBold(
                                                    text = it,
                                                    sized = 16,
                                                    textAlign = TextAlign.End,
                                                    colors = if (isSystemInDarkTheme()) White else BlackMode
                                                )
                                            }

//                                        TextBold(
//                                            text = subscription?.endDate?.let {
//                                                konversiFormatTanggal(
//                                                    it
//                                                )
//                                            }
//                                                ?: "",
//                                            sized = 16,
//                                            textAlign = TextAlign.End,
//                                            colors = if (isSystemInDarkTheme()) White else BlackMode
//                                        )
//
//
//
//                                        Spacer(modifier = Modifier.padding(8.dp))
//
//                                        ButtonGreen(onClick = {
//                                            // Navigasi atau aksi lainnya
//                                        }, text = "Lihat Detail", isLoading = false)
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextBold(
                                        text = subscription?.endDate?.let {
                                            konversiFormatTanggal(
                                                it
                                            )
                                        }
                                            ?: "",
                                        sized = 16,
                                        textAlign = TextAlign.End,
                                        colors = if (isSystemInDarkTheme()) White else BlackMode
                                    )
                                    Spacer(modifier = Modifier.padding(8.dp))
                                    ButtonBorderGreen(onClick = { /*TODO*/ }, text = "Lihat Detail", modifier = Modifier.width(120.dp))
//                                    ButtonGreen(onClick = {
//                                        // Navigasi atau aksi lainnya
//                                    }, text = "Lihat Detail", isLoading = false)
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
                            text = "Tidak ada langganan yang aktif saat ini 😉",
                            sized = 14,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun HistorySubscription(
    viewModel: LanggananScreenViewModel,
    initialScroll: Int,
    onScrollChanged: (Int) -> Unit,
    savedSubscriptionsHistory: Result<ListHistorySubscriptionResponse>?,
    idUser: String,
    onSaveSubscriptionsHistory: (Result<ListHistorySubscriptionResponse>) -> Unit // Tambahkan ini
) {
    val scrollState = rememberScrollState(initialScroll)

    val subscriptionsHistoryState by viewModel.subscriptionsHistory.collectAsState()

    LaunchedEffect(idUser) {
        if (savedSubscriptionsHistory == null) {
            viewModel.fetchSubscriptionsHistory(idUser)
        }
    }

    LaunchedEffect(subscriptionsHistoryState) {
        if (subscriptionsHistoryState is Result.Success) {
            onSaveSubscriptionsHistory(subscriptionsHistoryState) // Perbarui di TabScreen
        }
    }

    val displayedSubscriptionsState = savedSubscriptionsHistory ?: subscriptionsHistoryState

    Box(modifier = Modifier.fillMaxSize()) {
        when (displayedSubscriptionsState) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedLoading(modifier = Modifier.size(120.dp))
                        TextBold(
                            text = "Sedang memuat...",
                            sized = 14,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is Result.Success -> {
                val subscriptions =
                    (displayedSubscriptionsState as Result.Success<ListHistorySubscriptionResponse>).data

                if (subscriptions.data?.isEmpty() == true) {
                    Text(
                        text = "Tidak ada langganan aktif",
                        modifier = Modifier,
                        color = Color.Gray
                    )
                } else {
                    subscriptions.data?.forEach { subscription ->
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
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Jika ada gambar di subscription, tampilkan
                                    AsyncImage(
                                        model = subscription?.imageUrl ?: "",
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
                                            .padding(8.dp, 0.dp)
                                            .fillMaxWidth()
                                    ) {
                                        TextBold(
                                            text = subscription?.subscriptions?.title ?: "",
                                            colors = PrimaryGreen,
                                            sized = 18,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.padding(0.dp, 8.dp)
                                        )

                                        TextBold(
                                            text = subscription?.status ?: "",
                                            sized = 16,
                                            textAlign = TextAlign.End,
                                            colors = if (isSystemInDarkTheme()) White else BlackMode
                                        )

                                        Spacer(modifier = Modifier.padding(8.dp))

                                        ButtonGreen(onClick = {
                                            // Navigasi atau aksi lainnya
                                        }, text = "Lihat Detail", isLoading = false)
                                    }
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
                            text = "Tidak ada riwayat langganan 😉",
                            sized = 14,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
