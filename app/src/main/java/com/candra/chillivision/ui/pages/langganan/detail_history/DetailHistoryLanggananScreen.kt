package com.candra.chillivision.ui.pages.langganan.detail_history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.component.daysRemaining
import com.candra.chillivision.component.konversiFormatTanggal
import com.candra.chillivision.component.konversiFormatTanggal2
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.Red
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailHistoryLanggananScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailHistoryLanggananScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    id: String,
    title: String,
    price: String,
    startDate: String,
    endDate: String,
    description: String,
    statusTransaction: String,
    paymentMethod: String,
    period: String,
    urlImageSubscription: String,
    urlImageTransaction: String
) {
    // id
    // Nama Paket
    // Harga
    // Tanggal Pesan : Start Date
    // Tanggal Berakhir : End Date
    // Description
    // Status transaksi
    // Payment Method
    // Gambar
    // Gambar transksi,
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextBold(
                        "Detail Riwayat Langganan",
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

                modifier = Modifier.shadow(1.dp), // Shadow manual
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                    scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
                )
            )
        },
        containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = urlImageSubscription,
                            contentDescription = "Image Subscription",
                            modifier = Modifier
                                .size(120.dp)
                                .weight(0.5f)
                                .padding(16.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f)
                        ) {
                            TextBold(
                                text = "$title - $period Bulan",
                                sized = 18,
                                textAlign = TextAlign.Justify
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            TextBold(text = price, sized = 14, colors = PrimaryGreen)
                            Spacer(modifier = Modifier.padding(8.dp))
                            TextBold(
                                text = when (statusTransaction) {
                                    "active" -> "\uD83D\uDFE2 Aktif hingga ${
                                        konversiFormatTanggal(
                                            endDate
                                        )
                                    }"

                                    "pending" -> "🟡 Menunggu Konfirmasi"
                                    "success" -> "Berhasil"
                                    "expired" -> "🔴 Kadaluarsa"
                                    "cancel" -> "Dibatalkan"
                                    else -> "-"
                                },
                                sized = 14,
                                textAlign = TextAlign.Justify,
                                colors = if (isSystemInDarkTheme()) White else BlackMode
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            if (statusTransaction != "active" || statusTransaction != "pending" || statusTransaction != "cancel") {
                                TextBold(
                                    text = "Berakhir pada : " + konversiFormatTanggal2(endDate),
                                    sized = 14,
                                    colors = if (daysRemaining(endDate).toInt() <= 3) Red else PrimaryGreen
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(16.dp))
                TextBold(text = "Deskripsi Paket", sized = 16)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = description, style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Justify,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                        fontSize = 14.sp

                    )
                )
                Spacer(modifier = Modifier.padding(16.dp))
                TextBold(text = "Transaksi Pembayaran Paket", sized = 16)
                Spacer(modifier = Modifier.padding(8.dp))
                AsyncImage(
                    model = urlImageTransaction,
                    contentDescription = "Image Transaction",
                    modifier = Modifier.aspectRatio(1f)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                TextRegular(
                    text = "Tanggal Pembayaran Paket : " + konversiFormatTanggal2(startDate),
                    sized = 16
                )
                Spacer(modifier = Modifier.padding(4.dp))
                TextRegular(
                    text = "Metode Pembayaran : " + when (paymentMethod) {
                        "transfer" -> "Transfer Rekening"
                        "cash" -> "Tunai"
                        "credit" -> "Kredit"
                        else -> "-"
                    },
                    sized = 16,
                    textAlign = TextAlign.End,
                    colors = if (isSystemInDarkTheme()) White else BlackMode
                )
                Spacer(modifier = Modifier.padding(8.dp))

            }
        }
    }
}