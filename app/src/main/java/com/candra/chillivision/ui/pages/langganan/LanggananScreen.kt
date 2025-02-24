package com.candra.chillivision.ui.pages.langganan

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.candra.chillivision.R
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.subscriptions.SubscriptionsGetAll
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White

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
        TabScreen(viewModel)
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

@Composable
fun TabScreen(viewModel: LanggananScreenViewModel) {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Beli", "Aktif", "Riwayat")
    val icons = listOf(Icons.Default.ShoppingCart, Icons.Default.Star,
        Icons.AutoMirrored.Filled.List
    )
    val context = LocalContext.current

    // ✅ Menyimpan posisi scroll untuk masing-masing tab
    val scrollStates = rememberSaveable { mutableStateOf(mapOf(0 to 0, 1 to 0)) }

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
                        // ✅ Simpan posisi scroll sebelum pindah tab
                        scrollStates.value = scrollStates.value.toMutableMap().apply {
                            set(state, scrollStates.value[state] ?: 0)
                        }
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
            0 -> BeliLanggananScreen(
                context as LifecycleOwner,
                viewModel,
                scrollStates.value[state] ?: 0
            ) { newScroll ->
                scrollStates.value =
                    scrollStates.value.toMutableMap().apply { set(state, newScroll) }
            }

            1 -> LanggananSayaScreen(viewModel, scrollStates.value[state] ?: 0) { newScroll ->
                scrollStates.value =
                    scrollStates.value.toMutableMap().apply { set(state, newScroll) }
            }

            2 -> LanggananSayaScreen(viewModel, scrollStates.value[state] ?: 0) { newScroll ->
                scrollStates.value =
                    scrollStates.value.toMutableMap().apply { set(state, newScroll) }
            }
        }
    }
}

@Composable
fun BeliLanggananScreen(
    lifecycleOwner: LifecycleOwner,
    viewModel: LanggananScreenViewModel,
    initialScroll: Int,
    onScrollChanged: (Int) -> Unit
) {
    var subscriptions by remember { mutableStateOf<List<SubscriptionsGetAll>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.getSubscriptions().observe(lifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    isLoading = true
                }
                is Result.Success -> {
                    val data = result.data.data
                    if (data != null && subscriptions.isEmpty()) {
                        subscriptions = data
                    }
                    isLoading = false
                }
                is Result.Error -> {
                    Log.d("BeliLanggananScreen", "Error: ${result.errorMessage}")
                    isLoading = false
                }
                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(0.dp, 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subscriptions) { subscription ->
                    SubscriptionCard(subscription)
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(subscription: SubscriptionsGetAll) {
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
                .padding(8.dp)
        ) {
            TextBold(
                text = subscription.title ?: "",
                colors = PrimaryGreen,
                sized = 18,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = subscription.urlImage),
                    contentDescription = "Subscription Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = subscription.description ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 10.sp,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                            fontWeight = FontWeight.Normal,
                            color = if (isSystemInDarkTheme()) PrimaryGreen else Color.Black
                        ),
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextBold(
                        text = "${subscription.price?.let { formatRupiah(it) }}",
                        sized = 12,
                        textAlign = TextAlign.Start,
                        colors = if (isSystemInDarkTheme()) White else BlackMode
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextBold(text = "Beli", colors = White, sized = 12)
                    }
                }
            }
        }
    }
}


@Composable
fun LanggananSayaScreen(
    viewModel: LanggananScreenViewModel,
    initialScroll: Int,
    onScrollChanged: (Int) -> Unit
) {
    val scrollState = rememberScrollState(initialScroll)

    LaunchedEffect(scrollState.value) {
        onScrollChanged(scrollState.value)
    }

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        for (i in 1..100) {
            TextBold(text = "Langganan Saya $i", colors = PrimaryGreen, sized = 18)
            Divider()
        }
    }
}


// Function untuk membuat titik untuk Rp. 1.000.000 dari 1000000
fun formatRupiah(number: Int): String {
    val reverse = number.toString().reversed()
    val reverseWithDot = reverse.chunked(3).joinToString(".").reversed()
    return "Rp.$reverseWithDot,00"
}