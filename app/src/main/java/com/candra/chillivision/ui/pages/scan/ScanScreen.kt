package com.candra.chillivision.ui.pages.scan

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.SweetAlertComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.compressImage
import com.candra.chillivision.component.createImageUri
import com.candra.chillivision.component.handleCameraPermission
import com.candra.chillivision.component.uriToFile
import com.candra.chillivision.data.common.Result
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ScanScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(
            context = LocalContext.current,
            apiType = "model"
        )
    )
) {
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    var imageUri by remember { mutableStateOf<Uri?>(null) } // URI
//    var isLoading by remember {
//        mutableStateOf(false)
//    }

    // Simpan URI agar tidak hilang saat izin diminta
    var capturedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Toast.makeText(context, "Gambar Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                capturedImageUri?.let { uri ->
                    navController.navigate("confirmScan?imageUri=${Uri.encode(uri.toString())}")
                }
            }
        }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            navController.navigate("confirmScan?imageUri=${Uri.encode(it.toString())}")
        }

//        imageUri = uri
//        val imageFile = imageUri?.let { uriToFile(it, context) }
//        if (imageFile != null) {
//            val compressedFile = compressImage(imageFile, 2048)
//
//            val mimeType = when (compressedFile.extension.lowercase()) {
//                "png" -> "image/png"
//                "jpg", "jpeg" -> "image/jpeg"
//                else -> "image/jpeg"
//            }
//
//            val requestImageToFile = compressedFile.asRequestBody(mimeType.toMediaType())
//            val multipartBody = MultipartBody.Part.createFormData(
//                "file", compressedFile.name, requestImageToFile
//            )
//
//            viewModel.sendPrediction(
//                file = multipartBody,
//            ).observe(lifecycleOwner) { result ->
//                when (result) {
//                    is Result.Loading -> {
//                        isLoading = true
//                    }
//
//                    is Result.Success -> {
//                        isLoading = false
//                        // Terima response
//                        Log.d("Scan Screen", "scanscreen: ${result.data.message}")
//                        Toast.makeText(context, "Data Success : ${result.data}", Toast.LENGTH_LONG).show()
//                    }
//
//                    is Result.Error -> {
//                        Log.e("Scan Screen", "scanscreen: ${result}")
//                        Toast.makeText(context, "Data Gagal : ${result}", Toast.LENGTH_LONG).show()
//
//                    }
//
//                    else -> {
//                        isLoading = false
//                        Log.e("Scan Screen", "error: ${result}")
//                    }
//                }
//            }
//        }
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Izin Kamera Diberikan", Toast.LENGTH_SHORT).show()
            capturedImageUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Izin Kamera Ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp)) {
        TitleScan(modifier)
        Spacer(modifier = Modifier.height(32.dp))

        MenuScanIcon(
            isDarkTheme,
            R.drawable.potret_langsung,
            "Potret Langsung",
            "Dengan melakukan potret secara langsung, anda dapat mengetahui penyakit cabai secara langsung.",
            modifier,
            onClick = {
                val newUri = createImageUri(context)
                capturedImageUri = newUri
                handleCameraPermission(context, permissionLauncher, cameraLauncher, newUri)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        MenuScanIcon(
            isDarkTheme,
            R.drawable.upload_cloud,
            "Unggah Gambar",
            "Dengan menggunggah gambar tanaman cabai, maka anda dapat mengetahui penyakit yang ada pada tanaman cabai tersebut.",
            modifier, onClick = {
                launcher.launch("image/*")
            }
        )
    }
}

@Composable
private fun TitleScan(modifier: Modifier = Modifier) {
    TextBold(text = "Pindai", colors = PrimaryGreen, sized = 18)
}

@Composable
private fun MenuScanIcon(
    isDarkTheme: Boolean,
    icon: Int,
    title: String,
    desc: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSystemInDarkTheme()) BlackMode else WhiteSoft)
            .border(
                width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Tanya AI",
            modifier = Modifier
                .padding(16.dp)
                .size(35.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextBold(
                text = title,
                sized = 12,
                textAlign = TextAlign.Start,
                colors = if (isSystemInDarkTheme()) White else BlackMode
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkTheme) PrimaryGreen else Color.Black
                ),
                textAlign = TextAlign.Justify
            )
        }
    }
}

