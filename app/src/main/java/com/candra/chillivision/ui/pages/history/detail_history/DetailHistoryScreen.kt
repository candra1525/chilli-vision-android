package com.candra.chillivision.ui.pages.history.detail_history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.component.convertIsoToDateTime
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.Red

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailHistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailHistoryScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            LocalContext.current
        )
    ),
    idHistory: String? = null,
    title: String? = null,
    description: String? = null,
    urlImage: String? = null,
    createdAt: String? = null
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var isLoading by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HeaderComponent(
            text = "Detail Riwayat",
            modifier = modifier,
            navController = navController,
            icon = Icons.Filled.Delete,
            iconColor = Red,
            fontSized = 18,
            onIconClick = {
                SweetAlertComponent(
                    context = context,
                    title = "Hapus Riwayat",
                    contentText = "Apakah anda yakin ingin menghapus riwayat ini?",
                    type = "perhatian",
                    confirmYes = {
                        viewModel.deleteHistory(idHistory = idHistory ?: "")
                            .observe(context as LifecycleOwner) { result ->
                                when (result) {
                                    is Result.Loading -> {
                                        // Do nothing
                                        isLoading = true
                                    }

                                    is Result.Success -> {
                                        SweetAlertComponent(
                                            context = context,
                                            title = "Berhasil",
                                            contentText = "Riwayat berhasil dihapus",
                                            type = "success"
                                        )
                                        isLoading = false

                                        navController.navigate("history") {
                                            popUpTo(navController.graph.startDestinationId) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    }

                                    is Result.Error -> {
                                        SweetAlertComponent(
                                            context = context,
                                            title = "Gagal",
                                            contentText = result.errorMessage,
                                            type = "error"
                                        )
                                        isLoading = false
                                    }
                                }
                            }
                    }
                )
            },
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedLoading(modifier = Modifier.size(120.dp))
                    TextBold(
                        text = "Sedang memuat...",
                        sized = 14,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
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
                    if (urlImage != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f) // Sesuaikan dengan proporsi gambar
                                .clip(RoundedCornerShape(8.dp))
                                .padding(32.dp)
                        ) {
                            // Gambar utama
                            SubcomposeAsyncImage(
                                model = urlImage,
                                contentDescription = "Subscription Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize(), // Ukuran mengikuti Box
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center // Loading ditaruh di tengah gambar
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            AnimatedLoading(modifier = Modifier.size(100.dp)) // Ukuran loading tetap 100dp
                                            TextBold(text = "Memuat...", sized = 16)
                                        }
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(8.dp))
                    if (title != null) {
                        TextBold(text = title, sized = 24, textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    if (createdAt != null) {
                        TextBold(text = convertIsoToDateTime(createdAt))
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    if (description != null) {
                        TextRegular(text = description, sized = 16, textAlign = TextAlign.Justify)
                    }

                }
            }
        }
    }
}

