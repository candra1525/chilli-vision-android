package com.candra.chillivision.ui.pages.terms_privacy.privacy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.candra.chillivision.component.HeaderComponent

@Composable
fun PrivacyScreen(modifier: Modifier , navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
    ) {
        Column(modifier = modifier) {
            // Header
            HeaderComponent("Kebijakkan Privasi", modifier, navController)

        }
    }
}

