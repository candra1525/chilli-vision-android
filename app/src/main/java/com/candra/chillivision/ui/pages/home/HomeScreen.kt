package com.candra.chillivision.ui.pages.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.InitialAvatar
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.createImageUri
import com.candra.chillivision.component.handleCameraPermission
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.GreenSoft
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeScreenViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    // Pastikan hanya membaca data sekali saat pertama kali dibuka
    val userPreferences by viewModel.getPreferences()
        .collectAsState(initial = null)

    // Cek apakah data sudah tersedia
    when {
        userPreferences == null -> {
            // Jika data belum dimuat, jangan tampilkan apapun (menghindari flicker)
            return
        }

        userPreferences?.token.isNullOrEmpty() -> {
            // Jika token kosong, langsung arahkan ke welcome (sekali saja)
            LaunchedEffect(Unit) {
                navController.navigate("welcome") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }

        else -> {
            Log.d("HomeScreen", "UserPreferences: $userPreferences")
            // Jika token ada, langsung render HomeScreen
            Column(
                modifier = modifier
                    .verticalScroll(scrollState)
                    .padding(start = 16.dp, end = 16.dp, bottom = 90.dp),
            ) {
                HeaderHomeScreen(
                    isDarkTheme,
                    userPreferences?.fullname ?: "",
                    userPreferences?.image ?: "",
                    navController
                )
                QuickAccess(isDarkTheme, navController, context)
                TanyaAI(isDarkTheme, navController)
                VideoTutorial(isDarkTheme, context)
            }
        }
    }
}


@Composable
private fun HeaderHomeScreen(
    isDarkTheme: Boolean,
    fullname: String,
    image: String,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .padding(vertical = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InitialAvatar(
            fullname = fullname,
            imageUrl = image,
            size = 35.dp,
            fs = 25
        )

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Hai, $fullname",
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = PrimaryGreen,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    fontWeight = FontWeight.Bold
                )
            )
            TextBold(
                text = "Semoga harimu menyenangkan!",
                sized = 12,
                colors = if (isDarkTheme) Color.White else Color.Black
            )
        }

        Image(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(25.dp)
                .clickable {
                    navController.navigate("notification")
                }
        )

    }
}

@Composable
private fun QuickAccess(isDarkTheme: Boolean, navController: NavController, context: Context) {
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


//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            Toast.makeText(context, "Izin Kamera Diberikan", Toast.LENGTH_SHORT).show()
//            capturedImageUri?.let { cameraLauncher.launch(it) }
//        } else {
//            Toast.makeText(context, "Izin Kamera Ditolak", Toast.LENGTH_SHORT).show()
//        }
//    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        if (cameraGranted && storageGranted) {
            val uri = createImageUri(context)
            capturedImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Izin kamera atau penyimpanan ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Akses Cepat",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Start,
            colors = if (isSystemInDarkTheme()) White else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuQuickAccess(
                title = "Potret\nLangsung",
                icon = R.drawable.potret_langsung,
                onClick = {
                    val newUri = createImageUri(context)
                    capturedImageUri = newUri
                    handleCameraPermission(context, permissionLauncher, cameraLauncher, newUri)
                })
            MenuQuickAccess(title = "Unggah\nGambar", icon = R.drawable.upload_cloud, onClick = {
                launcher.launch("image/*")
            })
        }
    }
}

@Composable
private fun MenuQuickAccess(title: String, icon: Int, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clip(RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = if (isSystemInDarkTheme()) BlackMode else WhiteSoft)
                .border(
                    width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    onClick()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "Icon 1",
                modifier = Modifier.size(35.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextBold(
            text = title,
            sized = 10,
            textAlign = TextAlign.Center,
            colors = if (isSystemInDarkTheme()) White else BlackMode
        )

    }
}


@Composable
private fun TanyaAI(isDarkTheme: Boolean, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Tanya AI",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Start, colors = if (isSystemInDarkTheme()) White else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuTanyaAI(isDarkTheme, navController)
    }
}

@Composable
private fun MenuTanyaAI(isDarkTheme: Boolean, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isDarkTheme) BlackMode else WhiteSoft)
            .border(width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp))
            .clickable { navController.navigate("chilliAI") }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.tanya_ai),
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
                text = "Chat Chilli AI",
                sized = 12,
                textAlign = TextAlign.Start,
                colors = if (isDarkTheme) White else BlackMode
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Anda bisa menanyakan hal yang anda ingin tanyakan terkait penyakit cabai",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkTheme) PrimaryGreen else Color.Black
                ),
                textAlign = TextAlign.Justify
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.tanya_ai),
            contentDescription = "Tanya AI",
            modifier = Modifier
                .padding(16.dp)
                .size(35.dp)
        )
    }
}

@Composable
private fun VideoTutorial(isDarkTheme: Boolean, context: Context) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Video Tutorial",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Start, colors = if (isSystemInDarkTheme()) White else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuVideoTutorial(
            title = "Penggunaan Chilli Vision",
            textDesc = "Silahkan memutar video berikut terlebih dahulu untuk memahami penggunaan Chilli Vision",
            icon = R.drawable.video_tutorial,
            isDarkTheme = isDarkTheme,
            context = context,
            link = "https://www.youtube.com/watch?v=95MGH9eWzsw"
        )
        MenuVideoTutorial(
            title = "Penggunaan Chilli AI",
            "Silahkan memutar video berikut apabila ingin mengetahui penggunaan Chilli AI",
            icon = R.drawable.video_tutorial,
            isDarkTheme = isDarkTheme,
            context = context,
            link = "https://www.youtube.com/watch?v=95MGH9eWzsw"
        )
    }
}

@Composable
private fun MenuVideoTutorial(
    title: String,
    textDesc: String,
    icon: Int,
    isDarkTheme: Boolean,
    context: Context,
    link: String
) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isDarkTheme) BlackMode else WhiteSoft)
            .border(
                width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Pastikan intent berjalan di task baru
                context.startActivity(intent)
            }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Video Tutorial",
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
                colors = if (isDarkTheme) White else BlackMode
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = textDesc, style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_regular)),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkTheme) PrimaryGreen else Color.Black
                ), textAlign = TextAlign.Justify
            )
        }
    }
}