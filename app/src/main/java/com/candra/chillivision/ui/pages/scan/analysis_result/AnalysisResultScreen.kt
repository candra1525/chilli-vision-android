package com.candra.chillivision.ui.pages.scan.analysis_result

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.candra.chillivision.component.MenuScan
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.component.createImageUri
import com.candra.chillivision.component.handleCameraPermission
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.analysisResult.AnalisisResultResponse
import com.candra.chillivision.data.response.analysisResult.DetectionsItem
import com.candra.chillivision.data.response.analysisResult.DetectionsSummaryItem
import com.candra.chillivision.data.response.historyAnalysis.CreateHistoryRequest
import com.candra.chillivision.data.response.historyAnalysis.HistoryDetail
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.service.MAX_SAVE_HISTORY_PREMIUM
import com.candra.chillivision.service.MAX_SAVE_HISTORY_REGULAR
import com.candra.chillivision.service.SUBSCRIPTION_MAX_USAGE_AI_PREMIUM
import com.candra.chillivision.service.SUBSCRIPTION_MAX_USAGE_AI_REGULAR
import com.candra.chillivision.ui.navigation.Screen
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

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

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val userPreferences by viewModel.getPreferences()
        .collectAsState(initial = null)

    val userId = userPreferences?.id

    if (userId != null) {
        viewModel.getCountHistoryUser(userId)
    }

    val countHistoryUser by viewModel.countHistoryUser.collectAsState()

    // Simpan URI agar tidak hilang saat izin diminta
    var capturedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Toast.makeText(context, "Gambar Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                capturedImageUri?.let { uri ->
                    navController.navigate("confirmScan?imageUri=${Uri.encode(uri.toString())}")
                }
            }
        }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            navController.navigate("confirmScan?imageUri=${Uri.encode(it.toString())}")
        }
    }


//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            Toast.makeText(context, "Izin Kamera Diberikan", Toast.LENGTH_SHORT).show()
//            capturedImageUri?.let { cameraLauncher.launch(it) }
//        } else {
//            Toast.makeText(context, "Izin Kamera Ditolak", Toast.LENGTH_SHORT).show()
//        }
//    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        if (cameraGranted && storageGranted) {
            val uri = createImageUri(context)
            capturedImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Izin kamera atau penyimpanan ditolak", Toast.LENGTH_SHORT).show()
        }
    }


    if (isLoading) {
        Loading()
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        TextBold(
                            "Hasil Deteksi",
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
                            showBottomSheet = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.more),
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
                            text = "Hasil Analisis Deteksi",
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
                        result.detectionsSummary?.forEachIndexed { index, detection ->
                            DetectionItemView(index = index, detection = detection)
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    // Memberikan weight agar setiap MenuScan memiliki 50% lebar
                    MenuScan(
                        isDarkTheme = isSystemInDarkTheme(),
                        icon = R.drawable.potret_langsung,
                        title = "Potret Langsung",
                        desc = "Ambil kembali gambar secara langsung.",
                        modifier = Modifier.fillMaxWidth(),  // Memberikan 50% lebar
                        onClick = {
                            val newUri = createImageUri(context)
                            capturedImageUri = newUri
                            handleCameraPermission(context, permissionLauncher, cameraLauncher, newUri)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))  // Jarak antar menu

                    MenuScan(
                        isDarkTheme = isSystemInDarkTheme(),
                        icon = R.drawable.upload_cloud,
                        title = "Unggah Gambar",
                        desc = "Unggah kembali gambar dari album.",
                        modifier = Modifier.fillMaxWidth(),  // Memberikan 50% lebar
                        onClick = {
                            launcher.launch("image/*")
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))  // Jarak antar menu

                    MenuScan(
                        isDarkTheme = isSystemInDarkTheme(),
                        icon = R.drawable.save,
                        title = "Simpan Gambar",
                        desc = "Gambar akan disimpan pada riwayat anda.",
                        modifier = Modifier.fillMaxWidth(),  // Memberikan 50% lebar
                        onClick = {
                            when (userPreferences?.subscriptionName) {
                                "Gratis" -> {
                                    SweetAlertComponent(context = context,
                                        title = "Peringatan!",
                                        contentText = "Saat ini anda menggunakan paket gratis 🥲, silahkan upgrade ke paket berbayar untuk menggunakan layanan ini 😉",
                                        type = "warning",
                                        isCancel = true,
                                    )
                                    return@MenuScan
                                }
                                "Paket Reguler" -> {
                                    if (countHistoryUser > MAX_SAVE_HISTORY_REGULAR) {
                                        SweetAlertComponent(context = context,
                                            title = "Peringatan!",
                                            contentText = "Saat ini anda sudah mencapai batas penggunaan maksimal penyimpanan yaitu ${MAX_SAVE_HISTORY_REGULAR}x tanya AI, silahkan upgrade ke paket lebih tinggi atau hapus riwayat yang tidak diperlukan dahulu 😉",
                                            type = "warning",
                                            isCancel = true,
                                        )
                                        return@MenuScan
                                    }
                                }
                                "Paket Premium" -> {
                                    if (countHistoryUser > MAX_SAVE_HISTORY_PREMIUM) {
                                        SweetAlertComponent(context = context,
                                            title = "Peringatan!",
                                            contentText = "Saat ini anda sudah mencapai batas penggunaan maksimal penyimpanan yaitu ${MAX_SAVE_HISTORY_PREMIUM}x , silahkan hapus riwayat yang tidak diperlukan dahulu 😉",
                                            type = "warning",
                                            isCancel = true,
                                        )
                                        return@MenuScan
                                    }
                                }
                            }

                            viewModel.createHistory(
                                request = CreateHistoryRequest(
                                    image = result.imageUrl ?: "",
                                    detection_time = result.detectionTime ?: "",
                                    user_id = userId ?: "",
                                    unique_name_disease = result.uniqueNameDisease ?: "",
                                    history_details = result.detectionsSummary?.map { detection ->
                                        HistoryDetail(
                                            name_disease = detection?.diseaseInfo?.namaPenyakit
                                                ?: "",
                                            another_name_disease = detection?.diseaseInfo?.namaLain
                                                ?: "",
                                            symptom = detection?.diseaseInfo?.gejala ?: "",
                                            reason = detection?.diseaseInfo?.penyebab ?: "",
                                            preventive_meansure = detection?.diseaseInfo?.tindakanPencegahan
                                                ?: "",
                                            source = detection?.diseaseInfo?.sumber ?: ""
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
                                        showBottomSheet = false
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
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DetectionItemView(index: Int, detection: DetectionsSummaryItem?) {
    val info = detection?.diseaseInfo

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        info?.namaPenyakit?.let { penyakit ->
            TextBold(
                text = penyakit,
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
            text = it.replace("\\n", "\n"), // ubah \n menjadi baris baru
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
            text = it.replace("\\n", "\n"), // ubah \n menjadi baris baru
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
            text = it.replace("\\n", "\n"), // ubah \n menjadi baris baru
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
            text = it.replace("\\n", "\n"), // ubah \n menjadi baris baru
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
