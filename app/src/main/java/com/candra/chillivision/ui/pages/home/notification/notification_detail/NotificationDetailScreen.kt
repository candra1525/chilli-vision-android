package com.candra.chillivision.ui.pages.home.notification.notification_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.component.convertIsoToDateTime
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.pages.home.notification.NotificationScreenViewModel
import com.candra.chillivision.ui.theme.PrimaryGreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: NotificationScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    title: String,
    description: String,
    datePublish: String,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HeaderComponent("Detail Notifikasi", modifier, navController)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextBold(
                    text = title,
                    sized = 24,
                    textAlign = TextAlign.Start,
                    colors = PrimaryGreen
                )

                Spacer(modifier = Modifier.padding(2.dp))
                TextBold(text = convertIsoToDateTime(datePublish), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.padding(8.dp))

                TextRegular(text = description, textAlign = TextAlign.Justify, sized = 14)
            }
        }
    }
}