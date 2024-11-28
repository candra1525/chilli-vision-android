package com.candra.chillivision.ui.pages.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.candra.chillivision.R
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize())
    {

        Text(
            text = "Chilli Vision",
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = PrimaryGreen,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = modifier.height(8.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_255),
            contentDescription = "Logo ${R.string.app_name}",
            modifier = modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
        )

        Column(modifier = Modifier.padding(32.dp)) {
            Text(
                text = "Hai,", style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    color = PrimaryGreen,
                ), modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selamat Datang di Chilli Vision 👋🏻",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_semibold)),
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            )
            {
                Text(
                    text = "Masuk", style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                        textAlign = TextAlign.Center,
                    ), modifier = Modifier.fillMaxWidth()
                )

            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "atau", style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_semibold)),
                    textAlign = TextAlign.Center
                ), modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 2.dp, // Ketebalan border
                        color = PrimaryGreen, // Warna border
                        shape = RoundedCornerShape(8.dp) // Bentuk sudut
                    ),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White, // Warna latar belakang tombol
                    contentColor = PrimaryGreen // Warna teks tombol
                )
            )
            {
                Text(
                    text = "Belum punya akun ? Yuk, Daftar !",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Dengan masuk atau mendaftar pada Chilli Vision, kamu menyetujui Ketentuan Layanan dan Kebijakan Privasi.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    textAlign = TextAlign.Justify,
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}