package com.candra.chillivision.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle

data class NavigationItem(
    val title : String,
    val icon : Painter,
    val screen : Screen,
    val contentDescription : String,
    val modifier: Modifier = Modifier,
)