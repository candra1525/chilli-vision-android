package com.candra.chillivision.ui.pages.home.tanyaAI

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.ui.theme.PrimaryGreen

@Composable
fun ChilliAIScreen(modifier: Modifier = Modifier, navController: NavController) {
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(start = 32.dp, end = 32.dp, bottom = 90.dp),
    ) {
        HeaderChilliAI(modifier = modifier, navController = navController)

    }
}


@Composable
private fun HeaderChilliAI(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "back header ${R.string.app_name}",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navController.popBackStack()
                },
        )

        Text(
            text = "Chilli AI", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                color = PrimaryGreen,
                textAlign = TextAlign.Center
            ), modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )

        Image(
            painter = painterResource(id = R.drawable.ask_question),
            contentDescription = "information ${R.string.app_name}",
            modifier = Modifier
                .size(24.dp)
                .fillMaxWidth()
                .align(Alignment.CenterEnd)
                .clickable { },
        )
    }
}