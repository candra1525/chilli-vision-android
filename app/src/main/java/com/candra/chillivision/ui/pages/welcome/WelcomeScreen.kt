package com.candra.chillivision.ui.pages.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chilli Vision",
                    modifier = Modifier.padding(32.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PrimaryGreen,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.chilli_vision_logo),
                    contentDescription = "Logo ${R.string.app_name}",
                    modifier = Modifier.size(150.dp)
                )
            }
        }
        // Bagian bawah layar
        BottomWelcome(navController)
    }
}

@Composable
fun BottomWelcome(navController: NavController) {
    Column(modifier = Modifier.padding(32.dp)) {
        Text(
            text = "Hai,", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                color = PrimaryGreen,
            ), modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selamat Datang di Chilli Vision 👋🏻",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_semibold)),
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                NextPages("login", navController)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        )
        {
            Text(
                text = "Masuk", style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    textAlign = TextAlign.Center,
                ), modifier = Modifier.fillMaxWidth()
            )

        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "atau", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_semibold)),
                textAlign = TextAlign.Center
            ), modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                NextPages("daftar", navController)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .border(
                    width = 1.dp,
                    color = PrimaryGreen,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                contentColor = PrimaryGreen
            )
        )
        {
            Text(
                text = "Belum punya akun ? Yuk, Daftar !",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )

        }

        Spacer(modifier = Modifier.height(24.dp))

        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                )
            ) {
                append("Dengan melanjutkan proses pendaftaran akun pada Chilli Vision, saya menyetujui semua ")
            }
            pushStringAnnotation(tag = "terms", annotation = "terms")
            withStyle(
                style = SpanStyle(
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Ketentuan Layanan")
            }
            pop()

            withStyle(
                style = SpanStyle(
                    color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                )
            ) {
                append(" dan ")
            }
            pushStringAnnotation(tag = "privacy", annotation = "privacy")
            withStyle(
                style = SpanStyle(
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("Kebijakan Privasi")
            }
            pop()
            withStyle(
                style = SpanStyle(
                    color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                )
            ) {
                append(".")
            }
        }


        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 9.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                textAlign = TextAlign.Justify,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun NextPages(rute: String, navController: NavController) {
    if (rute == "login") {
        navController.navigate("login")
    } else if (rute == "daftar") {
        navController.navigate("register")
    }
}