package com.candra.chillivision.ui.pages.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.ui.theme.PrimaryGreen

@Composable
fun HistoryScreen(modifier : Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp )) {
        TextBold(text = "History Screen", colors = PrimaryGreen, sized = 18)
    }
}