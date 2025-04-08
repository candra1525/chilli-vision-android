package com.candra.chillivision.ui.pages.history

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.ButtonGreen
import com.candra.chillivision.component.Loading
import com.candra.chillivision.component.NotFound
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.convertIsoToDateTime
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.model.UserModel
import com.candra.chillivision.data.response.historyAnalysis.HistoryAnalysis
import com.candra.chillivision.data.response.historyAnalysis.HistoryAnalysisResponse
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.navigation.Screen
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HistoryScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextBold(
                        "Riwayat",
                        colors = PrimaryGreen,
                        sized = 18
                    )
                },
                navigationIcon = {},
                actions = {},
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
                .padding(16.dp, 0.dp)
        ) {
            HistoryContent(viewModel = viewModel, navController = navController)
        }
    }

}


@Composable
private fun TitleHistory() {
    TextBold(text = "Riwayat", colors = PrimaryGreen, sized = 18)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryContent(viewModel: HistoryScreenViewModel, navController: NavController) {
    val context = LocalContext.current
    val userData by viewModel.getPreferences().collectAsState(initial = UserModel())
    val idUser = userData.id
    HistoryAnalysisContent(
        viewModel = viewModel,
        idUser = idUser,
        navController = navController,
        context = context
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryAnalysisContent(
    viewModel: HistoryScreenViewModel,
    idUser: String,
    navController: NavController,
    context: Context
) {
    val historyAnalysisState by viewModel.historyAnalisis.collectAsState()
    var shouldRefresh by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(idUser, shouldRefresh) {
        if (idUser.isNotEmpty() && (shouldRefresh || historyAnalysisState !is Result.Success)) {
            Log.d("HistoryScreen", "Fetching data...")
            viewModel.fetchHistoryAnalisis(idUser)
            shouldRefresh = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (historyAnalysisState) {
            is Result.Loading -> {
                Loading()
            }

            is Result.Success -> {
                val history = (historyAnalysisState as Result.Success).data.data ?: emptyList()
                if (isDeleting) {
                    Loading(text = "Sedang menghapus data...")
                } else {
                    PullToRefreshBox(isRefreshing = shouldRefresh, onRefresh = {
                        shouldRefresh = true
                    }) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(0.dp, 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(history) { historyAnalysis ->
                                if (historyAnalysis != null) {
                                    HistoryCard(
                                        viewModel = viewModel,
                                        historyAnalysis = historyAnalysis,
                                        navController = navController,
                                        context = context,
                                        onSuccess = {
                                            shouldRefresh = true
                                            isDeleting = false // Reset setelah delete sukses
                                        },
                                        onLoadingDelete = {
                                            isDeleting = true
                                        }
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
                            text = "Tidak ada riwayat yang tersimpan saat ini 😉",
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
private fun HistoryCard(
    viewModel: HistoryScreenViewModel,
    historyAnalysis: HistoryAnalysis,
    navController: NavController,
    context: Context,
    onSuccess: () -> Unit = {},
    onLoadingDelete: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        // Show Dialog
        SweetAlertComponent(
            context = context,
            title = "Hapus Riwayat",
            contentText = "Apakah Anda yakin ingin menghapus riwayat ini?",
            type = "perhatian",
            confirmYes = {
                // Do Something
                viewModel.deleteHistory(idHistory = historyAnalysis.id ?: "")
                    .observe(context as LifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                onLoadingDelete()
                            }

                            is Result.Success -> {
                                Log.d("HistoryScreen", "Delete Success, refreshing data...")
                                onSuccess()
                                SweetAlertComponent(
                                    context = context,
                                    title = "Berhasil",
                                    contentText = "Riwayat berhasil dihapus",
                                    type = "success",
                                )
                                showDialog = false
                            }

                            is Result.Error -> {
                                // Do something
                                showDialog = false
                            }
                        }
                    }

            },
            confirmNo = {
                showDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showDialog = true }
                )
            }
            .clickable {
                navController.navigate(
                    route = Screen.DetailHistory.route
                        .replace(
                            "{idHistory}",
                            historyAnalysis.id.toString()
                        )
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    TextBold(
                        text = historyAnalysis.uniqueNameDisease ?: "",
                        colors = PrimaryGreen,
                        sized = 14,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )

                    Text(
                        text = historyAnalysis.detectionTime ?: "",
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Justify,
                            fontSize = 10.sp,
                            color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                        ),
                    )
                    historyAnalysis.createdAt?.let {
                        Text(
                            modifier = Modifier,
                            text = convertIsoToDateTime(it),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.Start,
                                fontSize = 10.sp,
                                color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                            ),
                        )
                    }
                }
                AsyncImage(
                    model = historyAnalysis.image ?: "",
                    contentDescription = "Subscription Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
        }
    }
}

