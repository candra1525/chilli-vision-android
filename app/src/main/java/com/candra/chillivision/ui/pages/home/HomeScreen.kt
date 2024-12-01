package com.candra.chillivision.ui.pages.home

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.GreenSoft
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(start = 32.dp, end = 32.dp, bottom = 60.dp),
    ) {
        HeaderHomeScreen(isDarkTheme)
        QuickAccess(isDarkTheme)
        TanyaAI(isDarkTheme)
        VideoTutorial(isDarkTheme)

    }
}

@Composable
private fun HeaderHomeScreen(isDarkTheme: Boolean) {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hai, Candra",
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = PrimaryGreen,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    fontWeight = FontWeight.Bold
                )
            )
            TextBold(
                text = "Semoga harimu menyenangkan!",
                sized = 12,
                colors = if (isDarkTheme) Color.White else Color.Black
            )
        }

        Image(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = "Avatar",
            modifier = Modifier.size(25.dp)
        )

    }
}

@Composable
private fun QuickAccess(isDarkTheme: Boolean) {

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Akses Cepat",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Start,
            colors = if (isSystemInDarkTheme()) White else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuQuickAccess(title = "Potret\nLangsung", icon = R.drawable.potret_langsung)
            MenuQuickAccess(title = "Unggah\nGambar", icon = R.drawable.upload_cloud)
        }
    }
}

@Composable
private fun MenuQuickAccess(title: String, icon: Int) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clip(RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = if (isSystemInDarkTheme()) BlackMode else WhiteSoft)
                .border(
                    width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "Icon 1",
                modifier = Modifier.size(35.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextBold(
            text = title,
            sized = 10,
            textAlign = TextAlign.Center,
            colors = if (isSystemInDarkTheme()) White else BlackMode
        )

    }
}


@Composable
private fun TanyaAI(isDarkTheme: Boolean) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Tanya AI",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Start, colors = if (isSystemInDarkTheme()) White else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuTanyaAI(isDarkTheme)
    }
}

@Composable
private fun MenuTanyaAI(isDarkTheme: Boolean) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isDarkTheme) BlackMode else WhiteSoft)
            .border(
                width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.tanya_ai),
            contentDescription = "Tanya AI",
            modifier = Modifier
                .padding(16.dp)
                .size(35.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextBold(text = "Chat Chilli AI", sized = 12, textAlign = TextAlign.Start, colors = if (isDarkTheme) White else BlackMode)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Anda bisa menanyakan hal yang anda ingin tanyakan terkait penyakit cabai",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkTheme) PrimaryGreen else Color.Black
                ),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
private fun VideoTutorial(isDarkTheme: Boolean) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Video Tutorial",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Start, colors = if (isSystemInDarkTheme()) White else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuVideoTutorial(
            title = "Penggunaan Chilli Vision",
            textDesc = "Silahkan memutar video berikut terlebih dahulu untuk memahami penggunaan Chilli Vision",
            icon = R.drawable.video_tutorial,
            isDarkTheme = isDarkTheme
        )
        MenuVideoTutorial(
            title = "Penggunaan Chilli AI",
            "Silahkan memutar video berikut apabila ingin mengetahui penggunaan Chilli AI",
            icon = R.drawable.video_tutorial,
            isDarkTheme = isDarkTheme
        )
    }
}

@Composable
private fun MenuVideoTutorial(title: String, textDesc: String, icon: Int, isDarkTheme: Boolean) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isDarkTheme) BlackMode else WhiteSoft)
            .border(
                width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Video Tutorial",
            modifier = Modifier
                .padding(16.dp)
                .size(35.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextBold(text = title, sized = 12, textAlign = TextAlign.Start, colors = if (isDarkTheme) White else BlackMode)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = textDesc, style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkTheme) PrimaryGreen else Color.Black
                ), textAlign = TextAlign.Justify
            )
        }
    }
}
