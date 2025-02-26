package com.candra.chillivision.ui.pages.langganan.detail

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.ButtonCustomColorWithIcon
import com.candra.chillivision.component.DatePlusMonths
import com.candra.chillivision.component.DateToday
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.dashedBorder
import com.candra.chillivision.component.directToWhatsapp
import com.candra.chillivision.component.formatRupiah
import com.candra.chillivision.component.konversiFormatTanggal
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.PrimaryGreen

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

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HeaderComponent("Detail Langganan", modifier, navController)
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
                        .padding(32.dp, 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(8.dp))

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Image Detail Langganan",
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.padding(16.dp))

                    TextBold(text = title, sized = 24)

                    Spacer(modifier = Modifier.padding(8.dp))

                    TextBold(text = formatRupiah(price), sized = 18, colors = PrimaryGreen)

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
                            TextBold(text = "Tanggal Mulai : ", sized = 12)
                            konversiFormatTanggal(DateToday())?.let {
                                TextBold(
                                    text = it,
                                    sized = 12
                                )
                            }

                            TextBold(text = "Tanggal Berhenti : ", sized = 12)
                            DatePlusMonths(
                                DateToday(),
                                period
                            )?.let {
                                konversiFormatTanggal(it)?.let {
                                    TextBold(
                                        text = it,
                                        sized = 12
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                            text = "WhatsApp Admin",
                            color = PrimaryGreen,
                            width = 180.dp,
                            icon = Icons.Outlined.Phone
                        )

                        if (!isBuy) {
                            ButtonCustomColorWithIcon(
                                onClick = {
                                    isBuy = true
                                },
                                text = "Beli Sekarang",
                                color = PrimaryGreen,
                                width = 180.dp,
                                icon = Icons.Filled.ShoppingCart
                            )
                        } else {
                            ButtonCustomColorWithIcon(
                                onClick = {
                                    isBuy = false
                                    isUpload = false
                                    capturedImageUri = null
                                    SweetAlertComponent(context = context, title = "Berhasil", contentText = "Bukti transaksi berhasil disimpan", type = "success", isCancel = false)
                                },
                                text = "Yakin Beli",
                                color = PrimaryGreen,
                                width = 180.dp,
                                icon = Icons.Filled.ShoppingCart
                            )
                        }

                    }
                }
            }
        }
    }
}

