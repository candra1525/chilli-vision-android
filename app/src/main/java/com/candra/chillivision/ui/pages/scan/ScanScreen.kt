package com.candra.chillivision.ui.pages.scan

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.candra.chillivision.R
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft

@Composable
fun ScanScreen(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    Column(modifier = modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp)) {
        TitleScan(modifier)
        Spacer(modifier = Modifier.height(32.dp))
        MenuScanIcon(isDarkTheme, R.drawable.potret_langsung, "Potret Langsung", "Dengan melakukan potret secara langsung, anda dapat mengetahui penyakit daun cabai secara langsung.", modifier)
        Spacer(modifier = Modifier.height(24.dp))
        MenuScanIcon(isDarkTheme, R.drawable.upload_cloud, "Unggah Gambar", "Dengan menggunggah gambar daun cabai, anda dapat mengetahui penyakit daun cabai.", modifier)
    }

}

@Composable
private fun TitleScan(modifier: Modifier = Modifier) {
    TextBold(text = "Pindai", colors = PrimaryGreen, sized = 18)
}

@Composable
private fun MenuScanIcon(isDarkTheme: Boolean, icon : Int, title : String, desc : String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSystemInDarkTheme()) BlackMode else WhiteSoft)
            .border(
                width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
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
            TextBold(text = title, sized = 12, textAlign = TextAlign.Start, colors = if (isSystemInDarkTheme()) White else BlackMode)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = desc,
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
