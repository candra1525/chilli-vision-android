package com.candra.chillivision.ui.pages.history.detail_history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.component.ButtonGreen
import com.candra.chillivision.component.Disclaimer
import com.candra.chillivision.component.Loading
import com.candra.chillivision.component.NotFound
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.historyAnalysis.HistoryDetailItem
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.Orange
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.Red
import com.candra.chillivision.ui.theme.WhiteSoft
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailHistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailHistoryScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    ),
    idHistory: String? = null,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val detailHistoryState by viewModel.detailHistory.collectAsState()

    var isLoading by remember { mutableStateOf(false) }
    var shouldRefresh by remember { mutableStateOf(false) }

//    LaunchedEffect(idHistory) {
//        if (idHistory != null && detailHistoryState !is Result.Success) {
//            isLoading = true
//            viewModel.fetchDetailHistory(idHistory = idHistory)
//            isLoading = false
//        }
//    }
//
//    LaunchedEffect(shouldRefresh) {
//        if (idHistory != null) {
//            Log.d("Detail History Screen", "Refreshing data...")
//            viewModel.fetchDetailHistory(idHistory)
//            shouldRefresh = false
//        }
//    }
    LaunchedEffect(idHistory, shouldRefresh) {
        if (idHistory != null && (shouldRefresh || detailHistoryState !is Result.Success)) {
            Log.d("Detail History Screen", "Fetching detail history...")
            isLoading = true
            viewModel.fetchDetailHistory(idHistory = idHistory)
            isLoading = false
            shouldRefresh = false
        }
    }


    if (isLoading) {
        Loading()
        return
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextBold(
                        "Detail Riwayat",
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
                actions = {
                    val load = viewModel.isLoad.collectAsState().value
                    if (!load) {
                        IconButton(onClick = {
                            SweetAlertComponent(
                                context = context,
                                title = "Hapus Riwayat",
                                contentText = "Apakah anda yakin ingin menghapus riwayat ini?",
                                type = "perhatian",
                                confirmYes = {
                                    viewModel.deleteHistory(idHistory = idHistory ?: "")
                                        .observe(context as LifecycleOwner) { result ->
                                            when (result) {
                                                is Result.Loading -> {
                                                    // Do nothing
                                                    isLoading = true
                                                }

                                                is Result.Success -> {
                                                    SweetAlertComponent(
                                                        context = context,
                                                        title = "Berhasil",
                                                        contentText = "Riwayat berhasil dihapus",
                                                        type = "success"
                                                    )
                                                    isLoading = false

                                                    navController.navigate("history") {
                                                        popUpTo(navController.graph.startDestinationId) {
                                                            inclusive = false
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                }

                                                is Result.Error -> {
                                                    SweetAlertComponent(
                                                        context = context,
                                                        title = "Gagal",
                                                        contentText = result.errorMessage,
                                                        type = "error"
                                                    )
                                                    isLoading = false
                                                }
                                            }
                                        }
                                }
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Back",
                                tint = Red
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                    scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
                ),
                modifier = Modifier.shadow(1.dp)
            )
        },
        containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (detailHistoryState) {
                is Result.Loading -> {
                    Loading()
                }

                is Result.Success -> {
                    val detail = (detailHistoryState as Result.Success).data.data
                    PullToRefreshBox(isRefreshing = shouldRefresh, onRefresh = {
                        shouldRefresh = true
                    }) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .imePadding()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {

                                // Component Disclaimer
                                Disclaimer()

                                Spacer(modifier = Modifier.padding(8.dp))

                                detail?.image?.let {
                                    ImageAnalysis(
                                        linkImage = it
                                    )
                                }



                                Spacer(modifier = Modifier.padding(8.dp))
                                TextBold(
                                    text = "Hasil Analisis Deteksi",
                                    colors = PrimaryGreen,
                                    sized = 18,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                detail?.uniqueNameDisease?.let {
                                    TextRegular(
                                        text = it,
                                        sized = 16,
                                        textAlign = TextAlign.Justify,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }


                                // Waktu deteksi global (jika ada)
                                Spacer(modifier = Modifier.padding(8.dp))
                                TextBold(
                                    text = "Waktu Deteksi",
                                    colors = PrimaryGreen,
                                    sized = 18,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                detail?.detectionTime?.let {
                                    TextRegular(
                                        text = it,
                                        sized = 16,
                                        textAlign = TextAlign.Justify,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.padding(8.dp))

                                detail?.historyDetail?.forEachIndexed { index, detection ->
                                    DetectionItemView(
                                        index = index,
                                        info = detection
                                    )
                                }

                            }
                        }
                    }
                }

                is Result.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NotFound(modifier = Modifier.size(200.dp))
                            TextBold(
                                text = "Detail riwayat tidak ditemukan 🥲",
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

                    Log.e(
                        "DetailHistoryScreen",
                        "Error fetching detail history: ${(detailHistoryState as Result.Error).errorMessage}"
                    )
                }
            }

        }

    }
}


@Composable
fun DetectionItemView(index: Int, info: HistoryDetailItem?) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        info?.nameDisease?.let { penyakit ->
            TextBold(
                text = penyakit,
                sized = 18,
                colors = PrimaryGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        info?.anotherNameDisease?.let {
            TextRegular(
                text = "($it)",
                sized = 16,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    TextBold(text = "Gejala", colors = PrimaryGreen, sized = 18, textAlign = TextAlign.Start)
    Spacer(modifier = Modifier.padding(4.dp))
    info?.symptom?.let {
        TextRegular(
            text = it,
            sized = 16,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.padding(8.dp))

    TextBold(text = "Penyebab", colors = PrimaryGreen, sized = 18, textAlign = TextAlign.Start)
    Spacer(modifier = Modifier.padding(4.dp))
    info?.reason?.let {
        TextRegular(
            text = it,
            sized = 16,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.padding(8.dp))

    TextBold(
        text = "Tindakan Pencegahan",
        colors = PrimaryGreen,
        sized = 18,
        textAlign = TextAlign.Start
    )
    Spacer(modifier = Modifier.padding(4.dp))
    info?.preventiveMeasure?.let {
        TextRegular(
            text = it,
            sized = 16,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.padding(8.dp))

    TextBold(text = "Sumber", colors = PrimaryGreen, sized = 18, textAlign = TextAlign.Start)
    Spacer(modifier = Modifier.padding(4.dp))
    info?.source?.let {
        TextRegular(
            text = it,
            sized = 16,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.padding(16.dp))
}

@Composable
fun ImageAnalysis(modifier: Modifier = Modifier, linkImage: String = "") {
    AsyncImage(
        model = linkImage,
        contentDescription = "Image Hasil Analisis",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .zoomable(rememberZoomState())
    )
}


