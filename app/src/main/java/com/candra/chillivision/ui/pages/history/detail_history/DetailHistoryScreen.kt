package com.candra.chillivision.ui.pages.history.detail_history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.Red

@Composable
fun DetailHistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailHistoryScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    )
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current


    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HeaderComponent(
            text = "Detail Riwayat",
            modifier = modifier,
            navController = navController,
            icon = Icons.Filled.Delete,
            iconColor = Red,
            onIconClick = {
                SweetAlertComponent(
                    context = context,
                    title = "Hapus Riwayat",
                    contentText = "Apakah Anda yakin ingin menghapus riwayat ini?",
                    type = "perhatian",
                    confirmYes = {
                        SweetAlertComponent(
                            context = context,
                            title = "Berhasil",
                            contentText = "Riwayat berhasil dihapus",
                            type = "success"
                        )
                    }


                )
            },
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextBold(text = "Lorem 100")
            }
        }
    }
}

