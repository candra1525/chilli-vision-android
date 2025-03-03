package com.candra.chillivision.ui.pages.history

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    Column(
        modifier = modifier
            .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 90.dp),
    ) {
        TitleHistory()
        Spacer(modifier = Modifier.height(16.dp))
        HistoryContent(viewModel = viewModel, navController = navController)
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

    var savedHistoryAnalysis by rememberSaveable {
        mutableStateOf<Result<HistoryAnalysisResponse>?>(
            null
        )
    }

    var prevIdUser by rememberSaveable { mutableStateOf("") } // Simpan nilai agar tidak reset saat rotasi

    val idUser = userData.id

    LaunchedEffect(idUser) {
        if (idUser.isNotEmpty() && idUser != prevIdUser) {
            viewModel.fetchHistoryAnalisis(idUser)
            prevIdUser = idUser // Perbarui ID user sebelumnya
        }
    }

    HistoryAnalysisContent(
        viewModel = viewModel,
        savedHistoryAnalysis = savedHistoryAnalysis,
        idUser = idUser,
        onSavedHistoryAnalysis = {
            savedHistoryAnalysis = it
        },
        navController = navController,
        context = context
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryAnalysisContent(
    viewModel: HistoryScreenViewModel,
    savedHistoryAnalysis: Result<HistoryAnalysisResponse>?,
    idUser: String,
    onSavedHistoryAnalysis: (Result<HistoryAnalysisResponse>) -> Unit,
    navController: NavController,
    context: Context
) {
    val historyAnalysisState by viewModel.historyAnalisis.collectAsState()
    var shouldRefresh by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(idUser) {
        if (idUser.isNotEmpty() && historyAnalysisState !is Result.Success) {
            viewModel.fetchHistoryAnalisis(idUser)
        }
    }

    LaunchedEffect(shouldRefresh) {
        if (idUser.isNotEmpty()) {
            Log.d("HistoryScreen", "Refreshing data...")
            viewModel.fetchHistoryAnalisis(idUser)
            shouldRefresh = false // Reset setelah refresh
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
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(history) { historyAnalysis ->
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
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
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
                        text = historyAnalysis.title ?: "",
                        colors = PrimaryGreen,
                        sized = 14,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )

                    Text(
                        text = historyAnalysis.description ?: "",
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
                AsyncImage(
                    model = historyAnalysis.urlImage ?: "",
                    contentDescription = "Subscription Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                historyAnalysis.createdAt?.let {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = convertIsoToDateTime(it),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Start,
                            fontSize = 10.sp,
                            color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                            fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                        ),
                    )
                }

                ButtonGreen(
                    onClick = {
                        navController.navigate(
                            route = Screen.DetailHistory.route
                                .replace(
                                    "{idHistory}",
                                    historyAnalysis.id.toString()
                                ).replace(
                                    "{title}",
                                    historyAnalysis.title ?: ""
                                ).replace(
                                    "{description}",
                                    historyAnalysis.description ?: ""
                                ).replace(
                                    "{createdAt}",
                                    historyAnalysis.createdAt ?: ""
                                ).replace(
                                    "{urlImage}",
                                    URLEncoder.encode(
                                        historyAnalysis.urlImage ?: "",
                                        StandardCharsets.UTF_8.toString()
                                    )
                                )
                        )
                    },
                    text = "Lihat",
                    isLoading = false,
                    modifier = Modifier
                        .width(80.dp)
                        .height(30.dp)
                )
            }
        }
    }
}

