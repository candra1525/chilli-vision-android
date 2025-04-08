package com.candra.chillivision.ui.pages.profile.lainnya

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TentangAplikasi(modifier: Modifier = Modifier, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                TextBold(
                    "Tentang Aplikasi", colors = PrimaryGreen, sized = 18
                )
            }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryGreen
                    )
                }
            }, modifier = Modifier.shadow(1.dp), colors = TopAppBarDefaults.topAppBarColors(
                containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
            )
            )
        }, containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        AboutContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun AboutContent(modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) WhiteSoft else BlackMode

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = R.drawable.chilli_vision_logo,
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextBold(
                    "Chilli Vision", colors = textColor, sized = 20, textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextRegular(
                text = "Chilli Vision adalah sebuah aplikasi berbasis Android yang dirancang untuk membantu petani, peneliti, serta masyarakat umum dalam mendeteksi penyakit pada tanaman cabai secara cepat dan akurat.",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(18.dp))

            TextBold("Fitur Utama", colors = textColor, sized = 16)
            Spacer(modifier = Modifier.height(4.dp))
            listOf(
                "Deteksi penyakit otomatis melalui gambar.",
                "Hasil diagnosis cepat dan instan.",
                "Informasi penyakit dan solusi penanganan awal.",
                "Riwayat diagnosa pengguna.",
                "Antarmuka sederhana dan mudah digunakan."
            ).forEach {
                TextRegular(text = "• $it", colors = textColor, sized = 14)
            }
            Spacer(modifier = Modifier.height(12.dp))

            TextBold("Tujuan Pengembangan", colors = textColor, sized = 16)
            Spacer(modifier = Modifier.height(4.dp))
            TextRegular(
                text = "Chilli Vision dikembangkan sebagai solusi teknologi pertanian (AgriTech) untuk menjawab tantangan keterbatasan akses terhadap ahli pertanian atau agronom di berbagai wilayah.",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(12.dp))

            TextBold("Siapa yang Cocok Menggunakan?", colors = textColor, sized = 16)
            Spacer(modifier = Modifier.height(4.dp))
            listOf(
                "Petani cabai skala kecil hingga besar.",
                "Mahasiswa dan peneliti pertanian.",
                "Penyuluh pertanian.",
                "Pelaku usaha agribisnis.",
                "Masyarakat umum dengan minat pertanian."
            ).forEach {
                TextRegular(text = "• $it", colors = textColor, sized = 14)
            }
            Spacer(modifier = Modifier.height(12.dp))

            TextBold("Keunggulan Aplikasi", colors = textColor, sized = 16)
            Spacer(modifier = Modifier.height(4.dp))
            listOf(
                "Berbasis AI terbaru.",
                "Cepat dan bisa diakses kapan saja.",
                "Bisa digunakan tanpa koneksi internet (opsional).",
                "Gratis dan tanpa biaya langganan."
            ).forEach {
                TextRegular(text = "• $it", colors = textColor, sized = 14)
            }
            Spacer(modifier = Modifier.height(12.dp))

            TextBold("Tentang Pengembang", colors = textColor, sized = 16)
            Spacer(modifier = Modifier.height(4.dp))
            TextRegular(
                text = "Aplikasi ini dikembangkan oleh tim yang berfokus pada integrasi teknologi digital untuk sektor pertanian.",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextRegular(text = "Email: support@chillivision.id", colors = textColor, sized = 14)
            TextRegular(text = "WhatsApp: +62 895-6032-31365", colors = textColor, sized = 14)
        }
    }
}