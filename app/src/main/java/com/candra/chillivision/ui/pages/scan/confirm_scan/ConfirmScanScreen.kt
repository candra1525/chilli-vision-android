package com.candra.chillivision.ui.pages.scan.confirm_scan

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.candra.chillivision.R
import com.candra.chillivision.component.AnimatedLoading
import com.candra.chillivision.component.ButtonCustomColorWithIcon
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.compressImage
import com.candra.chillivision.component.dashedBorder
import com.candra.chillivision.component.uriToFile
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.response.analysisResult.AnalisisResultResponse
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.WhiteSoft
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScanScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ConfirmScanScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            context = LocalContext.current, apiType = "model"
        )
    )
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isLoading by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val imageUri by remember {
        mutableStateOf(navController.currentBackStackEntry?.arguments?.getString("imageUri"))
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextBold(
                        "Konfirmasi Deteksi",
                        colors = PrimaryGreen,
                        sized = 18
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryGreen
                        )
                    }
                },
                actions = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft,
                    scrolledContainerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
                ),
                modifier = Modifier.shadow(1.dp)
            )
        },
        containerColor = if (isSystemInDarkTheme()) BlackMode else WhiteSoft
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp, 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding(),
                contentAlignment = Alignment.Center
            ) {

                if (!isLoading) {
                    Column {
                        ContentDetection(imageUri)

                        Spacer(modifier = Modifier.padding(16.dp))

                        ButtonCustomColorWithIcon(
                            onClick = {
                                if (imageUri != null) {
                                    isLoading = true
                                    SendImageToDetect(
                                        context = context,
                                        imageUri = Uri.parse(imageUri),
                                        viewModel = viewModel,
                                        lifecycleOwner = lifecycleOwner
                                    ) { result ->
                                        if (result is AnalisisResultResponse) {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                key = "analysis_result",
                                                value = result
                                            )
                                            navController.navigate("analysisResult")
                                        }
                                        isLoading = false
                                    }

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Gambar tidak ditemukan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            text = "Deteksi",
                            color = PrimaryGreen,
                            icon = Icons.Filled.Search,
                            modifier = Modifier.padding(0.dp, 0.dp)
                        )
                    }
                }

                if (isLoading) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedLoading(modifier = Modifier.size(150.dp))
                        TextBold(text = "Gambar sedang di deteksi", sized = 14)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextBold(text = "Mohon Menunggu ...", sized = 14)
                    }
                }
            }
        }
    }

//    Column(
//        modifier = modifier.fillMaxSize()
//    ) {
//        if (!isLoading) {
//            HeaderComponent("Konfirmasi Deteksi", modifier, navController)
//        }
//
//
//    }
}

@Composable
fun ContentDetection(imageUri: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(0.dp, 0.dp)
            .dashedBorder(2.dp, 8.dp, PrimaryGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Hasil Scan",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.upload_cloud),
                    contentDescription = "camera_scan",
                    modifier = Modifier.size(100.dp),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextBold(text = "Dummy Image")
            }
        }
    }
}

fun SendImageToDetect(
    context: Context,
    imageUri: Uri?,
    viewModel: ConfirmScanScreenViewModel,
    lifecycleOwner: LifecycleOwner,
    onSuccessDetect: (AnalisisResultResponse?) -> Unit
) {
    val imageFile = imageUri?.let { uriToFile(it, context) }
    if (imageFile != null) {
        val compressedFile = compressImage(imageFile, 2048)

        val mimeType = when (compressedFile.extension.lowercase()) {
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            else -> "image/jpeg"
        }

        val requestImageToFile = compressedFile.asRequestBody(mimeType.toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file", compressedFile.name, requestImageToFile
        )

        viewModel.sendPrediction(multipartBody)
            .observe(lifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        Log.d("SendImageToDetect", "Loading...")
                    }

                    is Result.Success -> {
                        Log.d("SendImageToDetect", "Success: ${result.data}")
                        onSuccessDetect(result.data as? AnalisisResultResponse)
                    }

                    is Result.Error -> {
                        Log.e("SendImageToDetect", "Error: ${result}")
                        Toast.makeText(
                            context,
                            "Gagal mendeteksi: ${result.errorMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                        onSuccessDetect(null)
                    }

                    else -> {
                        Log.e("SendImageToDetect", "Unexpected state")
                        onSuccessDetect(null)
                    }
                }
            }
    }
}
