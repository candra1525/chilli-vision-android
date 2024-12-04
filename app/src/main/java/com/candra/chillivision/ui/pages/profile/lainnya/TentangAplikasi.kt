package com.candra.chillivision.ui.pages.profile.lainnya

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft

@Composable
fun TentangAplikasi(modifier: Modifier = Modifier, navController: NavController) {
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(start = 32.dp, end = 32.dp, bottom = 90.dp),
    ) {
        HeaderTentangAplikasi(modifier = modifier, navController = navController)
        ImageAboutApps(modifier)
        DescriptionAboutApps(modifier)

        FooterAboutApps(modifier)
    }
}

@Composable
private fun HeaderTentangAplikasi(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "back header ${R.string.app_name}",
            modifier = Modifier
                .size(24.dp)
                .clickable { navController.popBackStack() },
        )

        Text(
            text = "Tentang Aplikasi", style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                color = PrimaryGreen,
                textAlign = TextAlign.Center
            ), modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun ImageAboutApps(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(16.dp))

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.dummy_profile),
            contentDescription = "icon ${R.string.app_name}",
            modifier = modifier
                .size(100.dp)
        )

        TextBold(
            text = "Chilli Vision",
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            sized = 20
        )
    }
}

@Composable
private fun DescriptionAboutApps(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(32.dp))

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quas suscipit consectetur fuga delectus! Harum nulla expedita fugiat vero eveniet deleniti fugit, non error laborum eligendi! Suscipit dignissimos adipisci eveniet nostrum error officiis cupiditate quam rerum ducimus illo ipsum distinctio, eum doloribus impedit molestias. Quibusdam, impedit saepe dolore labore odio necessitatibus voluptate quasi voluptatibus aperiam fugiat nam in incidunt dolorum ab quisquam et nihil eligendi expedita delectus culpa totam eveniet ut officiis. Esse repellat explicabo nihil, doloremque dicta provident illum necessitatibus dolores eaque harum eius. Aliquam cum excepturi, nam, ea eaque, dolor deserunt atque porro soluta sapiente velit sed sit quaerat?\n" +
                    "\n" +
                    "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Quas suscipit consectetur fuga delectus! Harum nulla expedita fugiat vero eveniet deleniti fugit, non error laborum eligendi! Suscipit dignissimos adipisci eveniet nostrum error officiis cupiditate quam rerum ducimus illo ipsum distinctio, eum doloribus impedit molestias. Quibusdam, impedit saepe dolore labore odio necessitatibus voluptate quasi voluptatibus aperiam fugiat nam in incidunt dolorum ab quisquam et nihil eligendi expedita delectus culpa totam eveniet ut officiis. Esse repellat explicabo nihil, doloremque dicta provident illum necessitatibus dolores eaque harum eius. Aliquam cum excepturi, nam, ea eaque, dolor deserunt atque porro soluta sapiente velit sed sit quaerat?",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                color = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
                textAlign = TextAlign.Justify

            )
        )

    }
}

@Composable
private fun FooterAboutApps(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(32.dp))

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        TextBold(
            text = "Chilli Vision",
            colors = PrimaryGreen,
            sized = 14,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextBold(
            text = "Version 1.0.0",
            colors = if (isSystemInDarkTheme()) WhiteSoft else BlackMode,
            sized = 12,
            textAlign = TextAlign.Center
        )
    }
}