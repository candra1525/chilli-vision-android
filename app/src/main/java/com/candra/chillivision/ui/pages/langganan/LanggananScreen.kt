package com.candra.chillivision.ui.pages.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.candra.chillivision.R
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White

@Composable
fun LanggananScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier
        .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 60.dp)
        .verticalScroll(scrollState)) {
        LanggananTitle(modifier)
        Spacer(modifier = Modifier.height(16.dp))
        LanggananAktif()
        Spacer(modifier = Modifier.height(32.dp))
        BeliLanggananTitle()
        Spacer(modifier = Modifier.height(16.dp))
        BeliLangganan(
            title = "Paket Regular",
            desc = "1.\tPerangkat lunak dapat mendeteksi jenis penyakit pada tanaman cabai dengan maksimal request 50x per hari.\n2.\tPengguna dapat berinteraksi dengan Chat AI untuk melakukan tanya jawab seputar penyakit tanaman cabai dengan maksimal penggunaan 10 kali perhari.\n3.\tPengguna dapat menyimpan riwayat deteksi dengan maksimal  50 riwayat.\n4.\tBerlaku dalam rentang waktu 1 bulan sejak pembelian paket.", "Rp. 500.000,00"
        )
        Spacer(modifier = Modifier.height(16.dp))
        BeliLangganan(
            title = "Paket Premium",
            desc = "1.\tPerangkat lunak dapat mendeteksi jenis penyakit pada tanaman cabai dengan maksimal request 200 kali per hari.\n2.\tPengguna dapat berinteraksi dengan Chat AI untuk melakukan tanya jawab seputar penyakit tanaman cabai dengan maksimal penggunaan 50 kali per hari.\n3.\tPengguna dapat menyimpan riwayat deteksi dengan maksimal 200 riwayat.\n4.\tBerlaku dalam rentang waktu 6 bulan sejak pembelian paket.", "Rp. 1.000.000,00"
        )
    }
}

@Composable
private fun LanggananTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextBold(text = "Langganan Saya", colors = PrimaryGreen, sized = 18)
        Image(
            painter = painterResource(id = R.drawable.langganan),
            contentDescription = "Avatar",
            modifier = Modifier.size(25.dp)
        )
    }
}

@Composable
private fun LanggananAktif() {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isSystemInDarkTheme()) BlackMode else White)
            .border(
                width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.langganan),
            contentDescription = "langganan_saya_saat_ini",
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextBold(
                text = "Paket Gratis",
                sized = 12,
                textAlign = TextAlign.Start,
                colors = if (isSystemInDarkTheme()) White else BlackMode
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "1.\tPerangkat lunak dapat mendeteksi jenis penyakit tanaman cabai dengan maksimal request deteksi 5x per hari\n2.\tTidak tersedia fitur Chat AI.\n3.\tTidak tersedia fitur riwayat.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    fontWeight = FontWeight.Normal,
                ),
                textAlign = TextAlign.Justify
            )
        }
    }
}


@Composable
private fun BeliLanggananTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextBold(text = "Beli Langganan", colors = PrimaryGreen, sized = 18)

    }
}

@Composable
private fun BeliLangganan(title: String, desc: String, price : String) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isSystemInDarkTheme()) BlackMode else White)
            .border(
                width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.langganan),
            contentDescription = "langganan_saya_saat_ini",
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextBold(
                text = title,
                sized = 12,
                textAlign = TextAlign.Start,
                colors = if (isSystemInDarkTheme()) White else BlackMode
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = desc, style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    fontWeight = FontWeight.Normal,
                ), textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextBold(
                text = price,
                sized = 14,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                colors = if (isSystemInDarkTheme()) White else BlackMode
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.align(Alignment.End)) {
                Button(
                    onClick = {
                        /*TODO*/
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text(
                        text = "Beli", style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                            textAlign = TextAlign.Center,
                        ), modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }
}
