package com.candra.chillivision.ui.pages.terms_privacy.terms

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.ui.pages.terms_privacy.privacy.BulletListItem
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.GraySoft
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(modifier: Modifier, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextBold(
                        "KETENTUAN LAYANAN CHILLI VISION",
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
                modifier = Modifier.shadow(1.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                    scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
                )
            )
        },
        containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        TermsListSection(innerPadding = innerPadding)
    }
}

@Composable
fun TermsListSection(innerPadding: PaddingValues) {
    val sections = listOf(
        "1. Definisi" to listOf(
            "\"Aplikasi\" merujuk pada aplikasi Chilli Vision yang dikembangkan untuk membantu pengguna mendiagnosis penyakit pada tanaman cabai melalui teknologi pemrosesan citra dan kecerdasan buatan.",
            "\"Pengguna\" adalah setiap individu yang mengunduh, mengakses, atau menggunakan aplikasi ini.",
            "\"Kami\" merujuk pada tim pengembang Chilli Vision sebagai pemilik dan pengelola aplikasi."
        ),
        "2. Ruang Lingkup Layanan" to listOf(
            "Pengenalan gejala penyakit melalui gambar tanaman.",
            "Pemberian informasi atau saran berdasarkan hasil analisis.",
            "Fitur tambahan seperti profil pengguna, riwayat diagnosis, dan kontak bantuan."
        ),
        "3. Ketentuan Penggunaan" to listOf(
            "Tidak menggunakan aplikasi untuk tujuan ilegal atau melanggar hukum.",
            "Tidak mengunggah konten yang mengandung kekerasan, diskriminasi, atau pelanggaran hak cipta.",
            "Menggunakan data hanya untuk kepentingan pribadi atau edukatif, bukan komersial tanpa izin."
        ),
        "4. Hak Kekayaan Intelektual" to listOf(
            "Seluruh konten, kode, dan desain aplikasi merupakan hak milik pengembang.",
            "Pengguna dilarang menyalin atau mendistribusikan tanpa izin tertulis."
        ),
        "5. Batasan Tanggung Jawab" to listOf(
            "Hasil diagnosis bersifat prediktif dan tidak menggantikan ahli.",
            "Pengguna bertanggung jawab atas keputusan berdasarkan hasil aplikasi.",
            "Aplikasi disediakan sebagaimana adanya tanpa jaminan apapun."
        ),
        "6. Pembaruan dan Perubahan Layanan" to listOf(
            "Kami dapat memperbarui layanan dan ketentuan ini tanpa pemberitahuan sebelumnya.",
            "Disarankan untuk memeriksa pembaruan secara berkala."
        ),
        "7. Penangguhan atau Penghapusan Akun" to listOf(
            "Akun dapat ditangguhkan atau dihapus bila ditemukan pelanggaran.",
            "Tujuannya adalah menjaga integritas dan keamanan layanan."
        ),
        "8. Hukum yang Berlaku" to listOf(
            "Ketentuan ini tunduk pada hukum yang berlaku di Indonesia.",
            "Sengketa diselesaikan secara musyawarah atau jalur hukum sesuai UU."
        ),
        "9. Kontak Pengembang" to listOf(
            "Email: candradev2003@gmail.com",
            "WhatsApp: +62 895-6032-31365",
        )
    )

    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) WhiteSoft else BlackMode

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        item {
            TextBold(
                text = "Terakhir diperbarui: 08 April 2024",
                colors = textColor,
                sized = 16,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextRegular(
                text = "Dengan mengakses dan menggunakan aplikasi Chilli Vision, Anda menyetujui seluruh Ketentuan Layanan berikut.",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        sections.forEach { (title, items) ->
            item {
                TextBold(
                    text = title,
                    colors = textColor,
                    sized = 16,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
                items.forEach { item ->
                    BulletListItem(text = item, textColor = textColor)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = GraySoft,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            TextBold(
                text = "Penutup",
                colors = textColor,
                sized = 16,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextRegular(
                text = "Dengan menggunakan aplikasi Chilli Vision, Anda menyetujui seluruh isi Ketentuan Layanan ini.",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
        }
    }
}
