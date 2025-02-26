package com.candra.chillivision.ui.pages.scan

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.saveable.rememberSaveable
import com.candra.chillivision.component.createImageUri
import com.candra.chillivision.component.handleCameraPermission

@Composable
fun ScanScreen(modifier: Modifier = Modifier, navController: NavController) {
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current

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

