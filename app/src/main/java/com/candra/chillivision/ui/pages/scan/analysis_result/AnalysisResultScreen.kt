package com.candra.chillivision.ui.pages.scan.analysis_result

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.Disclaimer
import com.candra.chillivision.component.Loading
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.analysisResult.AnalisisResultResponse
import com.candra.chillivision.data.response.analysisResult.DetectionsItem
import com.candra.chillivision.data.response.historyAnalysis.CreateHistoryRequest
import com.candra.chillivision.data.response.historyAnalysis.HistoryDetail
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.navigation.Screen
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisResultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AnalysisResultScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    val result = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<AnalisisResultResponse>("analysis_result")

    if (result == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Error.route)
        }
        return
    }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isLoading by remember {
        mutableStateOf(false)
    }

    val userPreferences = viewModel.getPreferences().collectAsState(initial = null)
    val userId = userPreferences.value?.id

    if (isLoading) {
        Loading()
    } else {
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
                        IconButton(onClick = {
                            viewModel.createHistory(
                                request = CreateHistoryRequest(
                                    image = result.imageUrl ?: "",
                                    detection_time = result.detectionTime ?: "",
                                    user_id = userId ?: "",
                                    unique_name_disease = result.uniqueNameDisease ?: "",
                                    history_details = result.detections?.map { detection ->
                                        HistoryDetail(
                                            name_disease = detection?.diseaseInfo?.namaPenyakit
                                                ?: "",
                                            another_name_disease = detection?.diseaseInfo?.namaLain
                                                ?: "",
                                            symptom = detection?.diseaseInfo?.gejala ?: "",
                                            reason = detection?.diseaseInfo?.penyebab ?: "",
                                            preventive_meansure = detection?.diseaseInfo?.tindakanPencegahan
                                                ?: "",
                                            source = detection?.diseaseInfo?.sumber ?: "",
                                            confidence_score = detection?.confidence ?: ""
                                        )
                                    } ?: emptyList()
                                )
                            ).observe(context as LifecycleOwner) { result ->
                                when (result) {
                                    is Result.Loading -> {
                                        isLoading = true
                                    }

                                    is Result.Success -> {
                                        SweetAlertComponent(
                                            context = context,
                                            title = "Berhasil",
                                            contentText = "Hasil analisis berhasil disimpan pada riwayat",
                                            type = "success",
                                            confirmYes = { }
                                        )
                                        isLoading = false
                                    }

                                    is Result.Error -> {
                                        SweetAlertComponent(
                                            context = context,
                                            title = "Gagal",
                                            contentText = result.errorMessage,
                                            type = "error",
                                            confirmYes = { }
                                        )
                                        isLoading = false
                                    }
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_save_24),
                                contentDescription = "Back",
                                tint = PrimaryGreen
                            )
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


                        // Gambar analisis
                        result.imageUrl?.let { ImageAnalysis(linkImage = it) }

                        Spacer(modifier = Modifier.padding(8.dp))
                        TextBold(
                            text = "Jenis Penyakit Keseluruhan",
                            colors = PrimaryGreen,
                            sized = 18,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        result.uniqueNameDisease?.let {
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
                        result.detectionTime?.let {
                            TextRegular(
                                text = it,
                                sized = 16,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.padding(8.dp))
                        // Loop hasil deteksi penyakit
                        result.detections?.forEachIndexed { index, detection ->
                            DetectionItemView(index = index, detection = detection)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetectionItemView(index: Int, detection: DetectionsItem?) {
    val info = detection?.diseaseInfo

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        info?.namaPenyakit?.let { penyakit ->
            TextBold(
                text = "$penyakit (${detection.confidence})",
                sized = 18,
                colors = PrimaryGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        info?.namaLain?.let {
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
    info?.gejala?.let {
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
    info?.penyebab?.let {
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
    info?.tindakanPencegahan?.let {
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
    info?.sumber?.let {
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
    )
}
