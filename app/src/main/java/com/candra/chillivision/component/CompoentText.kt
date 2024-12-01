package com.candra.chillivision.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.candra.chillivision.R

@Composable
fun TextBold(text: String, sized: Int = 12, modifier: Modifier = Modifier, textAlign: TextAlign = TextAlign.Start, colors : Color = Color.Black) {
    Text(
        text = text, modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = sized.sp,
            fontFamily = FontFamily(Font(R.font.quicksand_bold)),
            fontWeight = FontWeight.Bold,
            textAlign = textAlign,
            color = colors
        )
    )
}