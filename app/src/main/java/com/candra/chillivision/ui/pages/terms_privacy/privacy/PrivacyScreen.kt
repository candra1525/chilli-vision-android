package com.candra.chillivision.ui.pages.terms_privacy.privacy

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.GraySoft
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(modifier: Modifier, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextBold(
                        "KEBIJAKKAN PRIVASI CHILLI VISION",
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
        InformasiListSection(innerPadding = innerPadding)
    }
}


@Composable
fun InformasiListSection(innerPadding: PaddingValues) {
    val informasiList = listOf(
        "Informasi Pribadi: Nama, alamat email, nomor telepon, dan informasi lainnya yang Anda berikan secara sukarela melalui formulir atau fitur dalam aplikasi.",
        "Informasi Teknis: Informasi perangkat, sistem operasi, alamat IP, serta aktivitas penggunaan aplikasi (log data).",
        "Data Gambar: Foto tanaman yang Anda unggah ke dalam aplikasi untuk keperluan diagnosis penyakit tanaman.",
        "Informasi Lokasi (opsional): Jika Anda mengaktifkan izin lokasi, kami dapat mengakses lokasi perangkat Anda untuk meningkatkan akurasi layanan.",
    )

    val informasiList2 = listOf(
        "Menyediakan layanan utama, seperti diagnosis penyakit tanaman cabai.",
        "Meningkatkan kualitas dan kinerja aplikasi.",
        "Memberikan dukungan teknis kepada pengguna.",
        "Mengembangkan fitur baru berdasarkan kebutuhan dan perilaku pengguna.",
    )

    val informasiList4 = listOf(
        "Penyedia layanan pihak ketiga yang membantu kami dalam pengoperasian aplikasi (misalnya: layanan penyimpanan cloud).",
        "Pihak berwenang apabila diwajibkan oleh hukum atau perintah pengadilan.",
    )

    val informasiList5 = listOf(
        "Mengakses dan memperbarui informasi pribadi Anda.",
        "Menarik kembali persetujuan penggunaan data (dalam batas tertentu).",
        "Meminta penghapusan data pribadi Anda dari sistem kami.",
    )

    val informasiList6 = listOf(
        "Akses Kamera dan Penyimpanan: Untuk mengambil dan mengunggah gambar tanaman.",
        "Akses Internet: Untuk memproses data diagnosis secara daring dan menampilkan hasil.",
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
            InformasiHeader(textColor)
        }

        item {
            InformasiSection(
                title = "1. Informasi yang Kami Kumpulkan",
                description = "Kami dapat mengumpulkan informasi pribadi dan non-pribadi dari pengguna aplikasi kami, termasuk namun tidak terbatas pada:",
                items = informasiList,
                textColor = textColor
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            InformasiSection(
                title = "2. Penggunaan Informasi",
                description = "Informasi yang dikumpulkan digunakan untuk tujuan sebagai berikut:",
                items = informasiList2,
                textColor = textColor
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            TextBold(
                text = "3. Penyimpanan dan Keamanan Data",
                colors = textColor,
                sized = 16,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextRegular(
                text = "Kami menggunakan langkah-langkah teknis dan organisasi yang wajar untuk melindungi informasi pribadi Anda dari akses, penggunaan, atau pengungkapan yang tidak sah. Data disimpan secara terenkripsi dan hanya diakses oleh pihak yang berwenang untuk keperluan yang sah.",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            InformasiSection(
                title = "4. Pembagian Informasi",
                description = "Kami tidak menjual, menyewakan, atau menukar informasi pribadi Anda kepada pihak ketiga. Namun, informasi Anda dapat dibagikan kepada:",
                items = informasiList4,
                textColor = textColor
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            InformasiSection(
                title = "5. Hak Pengguna",
                description = "Sebagai pengguna, Anda memiliki hak untuk:",
                items = informasiList5,
                textColor = textColor
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            InformasiSection(
                title = "6. Izin Aplikasi",
                description = "Semua izin hanya digunakan untuk kepentingan operasional aplikasi dan tidak disalahgunakan untuk kepentingan lain. Untuk menjalankan fungsi tertentu, aplikasi Chilli Vision mungkin meminta izin berikut:",
                items = informasiList6,
                textColor = textColor
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            TextBold(
                text = "7. Perubahan pada Kebijakan Privasi",
                colors = textColor,
                sized = 16,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextRegular(
                text = "Kami dapat memperbarui Kebijakan Privasi ini dari waktu ke waktu. Setiap perubahan akan diberitahukan melalui pembaruan di dalam aplikasi atau melalui saluran komunikasi resmi kami.",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            TextBold(
                text = "8. Kontak Kami",
                colors = textColor,
                sized = 16,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextRegular(
                text = "Jika Anda memiliki pertanyaan, keluhan, atau permintaan terkait kebijakan privasi ini, silakan hubungi kami melalui:",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextBold(
                text = "Email: candradev2003@gmail.com",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
            TextBold(
                text = "WhatsApp: +62 895-6032-31365",
                colors = textColor,
                sized = 14,
                textAlign = TextAlign.Justify
            )
        }


        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GraySoft,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            InformasiFooter(textColor = textColor)
        }
    }
}


@Composable
fun InformasiHeader(textColor: Color) {
    TextBold(
        text = "Terakhir diperbarui: 08 April 2024",
        colors = textColor,
        sized = 16,
        textAlign = TextAlign.Start
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextRegular(
        text = "Selamat datang di Chilli Vision. Kami menghargai privasi Anda dan berkomitmen untuk melindungi data pribadi yang Anda berikan kepada kami. Kebijakan Privasi ini menjelaskan bagaimana kami mengumpulkan, menggunakan, menyimpan, dan melindungi informasi Anda saat menggunakan aplikasi Chilli Vision.",
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

@Composable
fun InformasiSection(
    title: String,
    description: String,
    items: List<String>,
    textColor: Color
) {
    TextBold(
        text = title,
        colors = textColor,
        sized = 16,
        textAlign = TextAlign.Start
    )
    Spacer(modifier = Modifier.height(4.dp))
    TextRegular(
        text = description,
        colors = textColor,
        sized = 14,
        textAlign = TextAlign.Justify
    )
    Spacer(modifier = Modifier.height(4.dp))
    items.forEach { item ->
        BulletListItem(text = item, textColor = textColor)
    }
}

@Composable
fun BulletListItem(text: String, textColor: Color) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextRegular(
            text = "\u2022",
            colors = textColor,
            sized = 14,
            modifier = Modifier.padding(end = 8.dp)
        )
        TextRegular(
            text = text,
            colors = textColor,
            sized = 14,
            textAlign = TextAlign.Justify,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}


@Composable
fun InformasiFooter(textColor: Color) {
    TextBold(
        text = "Penutup",
        colors = textColor,
        sized = 16,
        textAlign = TextAlign.Start
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextRegular(
        text = "Dengan menggunakan aplikasi Chilli Vision, Anda menyetujui pengumpulan dan penggunaan informasi sesuai dengan Kebijakan Privasi ini.\n" +
                "\n",
        colors = textColor,
        sized = 14,
        textAlign = TextAlign.Justify
    )

}
