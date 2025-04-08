package com.candra.chillivision.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.candra.chillivision.ui.theme.Orange

@Composable
fun Disclaimer() {
    // Peringatan
    TextBold(
        text = "Peringatan !",
        colors = Orange,
        sized = 13,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )

    TextRegular(
        text = "Hasil prediksi yang ditampilkan mungkin tidak sepenuhnya akurat. " +
                "Jika Anda merasa ragu, disarankan untuk berkonsultasi dengan ahli guna memperoleh informasi yang lebih valid dan mendalam.",
        sized = 12,
        textAlign = TextAlign.Justify,
        modifier = Modifier.fillMaxWidth(),
    )
}