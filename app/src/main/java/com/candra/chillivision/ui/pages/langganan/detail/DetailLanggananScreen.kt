package com.candra.chillivision.ui.pages.langganan.detail

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.ButtonCustomColorWithIcon
import com.candra.chillivision.component.DatePlusMonths
import com.candra.chillivision.component.DateToday
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.Loading
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.compressImage
import com.candra.chillivision.component.dashedBorder
import com.candra.chillivision.component.directToWhatsapp
import com.candra.chillivision.component.formatRupiah
import com.candra.chillivision.component.konversiFormatTanggal
import com.candra.chillivision.component.konversiFormatTanggal2
import com.candra.chillivision.component.uriToFile
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.model.UserModel
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.Red
import com.candra.chillivision.ui.theme.WhiteSoft
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailLanggananScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailLanggananScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val userData by viewModel.getPreferences().collectAsState(initial = UserModel())
    val idUser = userData.id

    // Ambil idLangganan dari navigation arguments
    val idLangganan = remember {
        navController.currentBackStackEntry?.arguments?.getString("idLangganan") ?: ""
    }

    // Panggil fetchDetailSubscriptions saat idLangganan berubah
    LaunchedEffect(idLangganan) {
        if (idLangganan.isNotEmpty()) {
            viewModel.fetchDetailSubscriptions(idLangganan)
        }
    }

    // Observe StateFlow dari ViewModel
    val detailSubscriptionState by viewModel.detailSubscriptions.collectAsState()

    // State untuk menyimpan data
    var title by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var price by remember { mutableIntStateOf(0) }
    var period by remember { mutableIntStateOf(0) }
    var description by remember { mutableStateOf("") }
    var isLoaded by remember { mutableStateOf(false) }
    var isBuy by remember { mutableStateOf(false) }

    var isLoadingSubmit by remember { mutableStateOf(false) }

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUpload by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            capturedImageUri = it
            isUpload = true
        }
    }

    when (detailSubscriptionState) {
        is Result.Loading -> {
            isLoaded = true
        }

        is Result.Success -> {
            val data = (detailSubscriptionState as Result.Success).data.data
            title = data?.title ?: ""
            imageUrl = data?.urlImage ?: ""
            price = data?.price ?: 0
            period = data?.period ?: 0
            description = data?.description ?: ""
            isLoaded = false
        }

        is Result.Error -> {
            isLoaded = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextBold(
                        "Detail Langganan",
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
                actions = {},
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
        ) {
            if (!isLoaded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .imePadding()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Image Detail Langganan",
                            modifier = Modifier.size(150.dp)
                        )

                        Spacer(modifier = Modifier.padding(16.dp))

                        TextBold(text = "${title} - ${period} Bulan", sized = 20)

                        Spacer(modifier = Modifier.padding(8.dp))

                        TextBold(text = formatRupiah(price), sized = 16, colors = PrimaryGreen)

                        Spacer(modifier = Modifier.padding(8.dp))

                        Text(
                            text = description, style = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.Justify,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                                fontSize = 14.sp

                            )
                        )

                        if (isBuy) {
                            Spacer(modifier = Modifier.padding(8.dp))
                            TextBold(text = "Bukti Transaksi", sized = 16)
                            Spacer(modifier = Modifier.padding(8.dp))
                            if (isUpload) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .dashedBorder(2.dp, 2.dp, PrimaryGreen)
                                        .padding(8.dp)
                                        .clickable {
                                            launcher.launch("image/*")
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    AsyncImage(
                                        model = capturedImageUri,
                                        contentDescription = "image transaction"
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .dashedBorder(2.dp, 2.dp, PrimaryGreen)
                                        .padding(8.dp)
                                        .clickable {
                                            launcher.launch("image/*")
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.upload_cloud),
                                        contentDescription = "Upload",
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .size(100.dp)
                                    )
                                    TextBold(text = "Unggah Bukti Transaksi")
                                }
                            }

                            Spacer(modifier = Modifier.padding(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TextBold(text = "Tanggal Mulai", sized = 12)
                                    konversiFormatTanggal2(DateToday())?.let {
                                        TextBold(
                                            text = it,
                                            sized = 12
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TextBold(text = "Tanggal Berhenti", sized = 12)
                                    DatePlusMonths(
                                        DateToday(),
                                        period
                                    )?.let {
                                        konversiFormatTanggal2(it)?.let {
                                            TextBold(
                                                text = it,
                                                sized = 12
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.padding(16.dp))

                        if (isLoadingSubmit) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AnimatedLoading(modifier = Modifier.size(60.dp))
                                TextBold(
                                    text = "Sedang memproses...",
                                    sized = 11,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp, 0.dp, 0.dp, 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ButtonCustomColorWithIcon(
                                    onClick = {
                                        directToWhatsapp(
                                            context,
                                            "62895603231365",
                                            "Halo Admin 👋🏻, saya ingin bertanya terkait Paket Aplikasi Chilli Vision 🌶️"
                                        )
                                    },
                                    text = "Tanya Admin",
                                    color = PrimaryGreen,
                                    modifier = Modifier.weight(1f),
                                    icon = Icons.Outlined.Phone
                                )

                                Spacer(modifier = Modifier.padding(8.dp))

                                if (!isBuy) {
                                    ButtonCustomColorWithIcon(
                                        onClick = {
                                            isBuy = true
                                        },
                                        text = "Beli",
                                        color = PrimaryGreen,
                                        modifier = Modifier.weight(1f),
                                        icon = Icons.Filled.ShoppingCart
                                    )
                                } else {
                                    ButtonCustomColorWithIcon(
                                        onClick = {
                                            viewModel.checkSubscriptionActive(idUser)
                                                .observe(context as LifecycleOwner) { result ->
                                                    when (result) {
                                                        is Result.Loading -> {
                                                            isLoadingSubmit = true
                                                        }

                                                        is Result.Success -> {
                                                            val data =
                                                                (result as Result.Success).data.data
                                                            if (data != null) {
                                                                SweetAlertComponent(
                                                                    context = context,
                                                                    title = "Peringatan",
                                                                    contentText = "Anda sudah memiliki langganan aktif",
                                                                    type = "warning",
                                                                    isCancel = false
                                                                )
                                                                isLoadingSubmit = false
                                                            } else {
                                                                if (capturedImageUri == null || capturedImageUri == Uri.EMPTY || capturedImageUri == Uri.parse(
                                                                        ""
                                                                    )
                                                                ) {
                                                                    SweetAlertComponent(
                                                                        context = context,
                                                                        title = "Peringatan",
                                                                        contentText = "Silahkan unggah bukti transaksi",
                                                                        type = "warning",
                                                                        isCancel = false
                                                                    )
                                                                } else {
                                                                    isLoadingSubmit = true
                                                                    BuySubscription(
                                                                        context = context,
                                                                        viewModel = viewModel,
                                                                        idLangganan = idLangganan,
                                                                        idUser = idUser,
                                                                        period = period,
                                                                        capturedImageUri = capturedImageUri
                                                                    ) {
                                                                        isBuy = false
                                                                        isUpload = false
                                                                        capturedImageUri = null
                                                                        isLoadingSubmit = false
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        is Result.Error -> {
                                                            isLoadingSubmit = false
                                                            SweetAlertComponent(
                                                                context = context,
                                                                title = "Gagal",
                                                                contentText = (result as Result.Error).errorMessage
                                                                    ?: "Terjadi kesalahan",
                                                                type = "error",
                                                                isCancel = false
                                                            )
                                                        }
                                                    }
                                                }
                                        },
                                        text = "Yakin Beli",
                                        color = PrimaryGreen,
                                        modifier = Modifier.weight(1f),
                                        icon = Icons.Filled.ShoppingCart,
                                    )
                                }

                            }
                        }
                    }
                }
            }
            else {
                Loading()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun BuySubscription(
    context: Context,
    viewModel: DetailLanggananScreenViewModel,
    idLangganan: String,
    idUser: String,
    period: Int,
    capturedImageUri: Uri?,
    onSucess: () -> Unit
) {
    val imageFile = capturedImageUri?.let {
        uriToFile(
            imageUri = it,
            context = context
        )
    }

    if (imageFile != null) {
        val compressedFile = compressImage(imageFile, 2048)

        val mimeType = when (compressedFile.extension.lowercase()) {
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            else -> "image/jpeg" // Default
        }

        val requestImageToFile =
            compressedFile.asRequestBody(mimeType.toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "image_transaction", // harus sesuai dengan API Service
            compressedFile.name,
            requestImageToFile
        )

        viewModel.setCreateHistorySubscription(
            subscriptionId = idLangganan,
            userId = idUser,
            startDate = DateToday(),
            endDate = DatePlusMonths(
                DateToday(),
                period
            ).toString(),
            imageTransaction = multipartBody
        ).observe(context as LifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> { /* Tampilkan loading */

                }

                is Result.Success -> {
                    onSucess()
                    SweetAlertComponent(
                        context = context,
                        title = "Berhasil",
                        contentText = "Transaksi berhasil, silahkan lihat pada halaman riwayat langganan",
                        type = "success",
                        isCancel = false
                    )
                }

                is Result.Error -> {
                    SweetAlertComponent(
                        context = context,
                        title = "Gagal",
                        contentText = result.errorMessage
                            ?: "Terjadi kesalahan",
                        type = "error",
                        isCancel = false
                    )
                }
            }
        }
    }
}

