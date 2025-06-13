package com.candra.chillivision.ui.pages.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.candra.chillivision.R
import com.candra.chillivision.component.InitialAvatar
import com.candra.chillivision.component.MenuScan
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.TextRegular
import com.candra.chillivision.component.createImageUri
import com.candra.chillivision.component.handleCameraPermission
import com.candra.chillivision.component.hitungHariMenujuTanggal
import com.candra.chillivision.component.hitungSelisihHari
import com.candra.chillivision.component.konversiFormatTanggal
import com.candra.chillivision.data.vmf.ViewModelFactory
import com.candra.chillivision.service.SUBSCRIPTION_MAX_USAGE_AI_PREMIUM
import com.candra.chillivision.service.SUBSCRIPTION_MAX_USAGE_AI_REGULAR
import com.candra.chillivision.service.SUBSCRIPTION_MAX_USAGE_DETECT_PREMIUM
import com.candra.chillivision.service.SUBSCRIPTION_MAX_USAGE_DETECT_REGULAR
import com.candra.chillivision.ui.theme.BlackMode
import com.candra.chillivision.ui.theme.GraySoft
import com.candra.chillivision.ui.theme.GreenSoft
import com.candra.chillivision.ui.theme.PrimaryGreen
import com.candra.chillivision.ui.theme.Red
import com.candra.chillivision.ui.theme.White
import com.candra.chillivision.ui.theme.WhiteSoft
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
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
    val userPreferences by viewModel.getPreferences().collectAsState(initial = null)

    BackHandler(enabled = true) {
        ActivityCompat.finishAffinity(context as Activity)
    }

    when {
        userPreferences == null -> {
            return
        }

        userPreferences?.token.isNullOrEmpty() -> {
            LaunchedEffect(Unit) {
                navController.navigate("welcome") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }

        else -> {
            val maxCountUsageAI = when (userPreferences?.subscriptionName) {
                "Paket Reguler" -> SUBSCRIPTION_MAX_USAGE_AI_REGULAR
                "Paket Premium" -> SUBSCRIPTION_MAX_USAGE_AI_PREMIUM
                else -> null
            }

            val maxCountUsageDetect = when (userPreferences?.subscriptionName) {
                "Paket Reguler" -> SUBSCRIPTION_MAX_USAGE_DETECT_REGULAR
                "Paket Premium" -> SUBSCRIPTION_MAX_USAGE_DETECT_PREMIUM
                else -> null
            }

            val countUsageAIFloat = userPreferences?.countUsageAI?.toIntOrNull()?.let { usage ->
                val max = when (userPreferences?.subscriptionName) {
                    "Paket Reguler" -> SUBSCRIPTION_MAX_USAGE_AI_REGULAR
                    "Paket Premium" -> SUBSCRIPTION_MAX_USAGE_AI_PREMIUM
                    else -> null
                }
                if (max != null && max != 0) usage.toFloat() / max else 0f
            } ?: 0f

            val countUsageDetectFloat = userPreferences?.countUsageDetect?.toIntOrNull()?.let { usage ->
                val max = when (userPreferences?.subscriptionName) {
                    "Paket Reguler" -> SUBSCRIPTION_MAX_USAGE_DETECT_REGULAR
                    "Paket Premium" -> SUBSCRIPTION_MAX_USAGE_DETECT_PREMIUM
                    else -> null
                }
                if (max != null && max != 0) usage.toFloat() / max else 0f
            } ?: 0f


            val countUsageAIPercentage = (countUsageAIFloat * 100).roundToInt()
            val countUsageDetectPercentage = (countUsageDetectFloat * 100).roundToInt()

            val start = userPreferences?.startDateSubscription ?: ""
            val end = userPreferences?.endDateSubscription ?: ""
            val diffDaysFromNow = hitungHariMenujuTanggal(end) + 1.0f
            val totalDays = hitungSelisihHari(start, end)
            val dayPersentage = (diffDaysFromNow.toFloat() / totalDays.toFloat())

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
                Column(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDarkTheme) BlackMode else GreenSoft)
                ) {
                    TextBold(
                        text = "Penggunaan Harian Chilli Vision",
                        sized = 12,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        textAlign = TextAlign.Center,
                        colors = if (isSystemInDarkTheme()) White else BlackMode
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = countUsageDetectFloat,
                                    trackColor = if (isDarkTheme) WhiteSoft else GraySoft,
                                    strokeWidth = 6.dp,
                                    modifier = Modifier.size(80.dp),
                                    color = if (countUsageDetectFloat > 0.95f) Red else PrimaryGreen,
                                )

                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TextBold(
                                        text = "$countUsageDetectPercentage%", sized = 11
                                    )
                                    TextRegular(
                                        text = "${userPreferences?.countUsageDetect}/$maxCountUsageDetect",
                                        sized = 9
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            TextBold(text = "Deteksi 🔍", colors = PrimaryGreen, sized = 10)
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = countUsageAIFloat,
                                    trackColor = if (isDarkTheme) WhiteSoft else GraySoft,
                                    strokeWidth = 6.dp,
                                    modifier = Modifier.size(80.dp), 
                                    color = if (countUsageAIFloat > 0.9f) Red else PrimaryGreen,
                                )

                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TextBold(
                                        text = "$countUsageAIPercentage%", sized = 11
                                    )
                                    TextRegular(
                                        text = "${userPreferences?.countUsageAI}/$maxCountUsageAI",
                                        sized = 9
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            TextBold(text = "Tanya AI ✨", colors = PrimaryGreen, sized = 10)

                        }
                    }

                    if (userPreferences?.subscriptionName != "Gratis") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        ) {
                            TextBold(
                                text = "${userPreferences?.subscriptionName}",
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth(),
                                sized = 12
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            LinearProgressIndicator(
                                progress = dayPersentage,
                                modifier = Modifier.fillMaxWidth(),
                                trackColor = if (isDarkTheme) WhiteSoft else GraySoft,
                                color = if (dayPersentage < 0.03f) Red else PrimaryGreen,
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween // Untuk kiri dan kanan
                            ) {

                                TextRegular(
                                    text = "${
                                        userPreferences?.startDateSubscription?.let {
                                            konversiFormatTanggal(
                                                it
                                            )
                                        }
                                    }", sized = 10
                                )  // Teks di kiri bawah
                                TextRegular(
                                    text = "${
                                        userPreferences?.endDateSubscription?.let {
                                            konversiFormatTanggal(
                                                it
                                            )
                                        }
                                    }", sized = 10
                                )  // Teks di kiri bawah
                            }
                        }
                    }
                }
                QuickAccess(isDarkTheme, navController, context)
                TanyaAI(isDarkTheme, navController)
                VideoTutorial(isDarkTheme, context)
            }
        }
    }
}


@Composable
private fun HeaderHomeScreen(
    isDarkTheme: Boolean, fullname: String, image: String, navController: NavController
) {
    Row(
        modifier = Modifier
            .padding(vertical = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InitialAvatar(
            fullname = fullname, imageUrl = image, size = 35.dp, fs = 25
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
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.quicksand_bold)),
                    fontWeight = FontWeight.Bold
                )
            )
            TextRegular(
                text = "Semoga harimu menyenangkan!",
                sized = 12,
                colors = if (isDarkTheme) Color.White else Color.Black
            )
        }

        Image(painter = painterResource(id = R.drawable.notification),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(25.dp)
                .clickable {
                    navController.navigate("notification")
                })

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

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted =
            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        if (cameraGranted && storageGranted) {
            val uri = createImageUri(context)
            capturedImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Izin kamera atau penyimpanan ditolak", Toast.LENGTH_SHORT)
                .show()
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Akses Cepat",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Center,
            colors = if (isSystemInDarkTheme()) White else BlackMode
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Memberikan weight agar setiap MenuScan memiliki 50% lebar
            MenuScan(
                isDarkTheme = isSystemInDarkTheme(),
                icon = R.drawable.potret_langsung,
                title = "Potret Langsung",
                sizeTitle = 11,
                modifier = Modifier.weight(1f),  // Memberikan 50% lebar
                onClick = {
                    val newUri = createImageUri(context)
                    capturedImageUri = newUri
                    handleCameraPermission(
                        context,
                        permissionLauncher,
                        cameraLauncher,
                        newUri
                    )
                }
            )

            Spacer(modifier = Modifier.width(16.dp))  // Jarak antar menu

            MenuScan(
                isDarkTheme = isSystemInDarkTheme(),
                icon = R.drawable.upload_cloud,
                title = "Unggah Gambar",
                sizeTitle = 11,
                modifier = Modifier.weight(1f),  // Memberikan 50% lebar
                onClick = {
                    launcher.launch("image/*")
                }
            )
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
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Tanya AI",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Start,
            colors = if (isSystemInDarkTheme()) White else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuTanyaAI(isDarkTheme, navController)
    }
}

@Composable
private fun MenuTanyaAI(isDarkTheme: Boolean, navController: NavController) {
    Row(modifier = Modifier
        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(color = if (isDarkTheme) BlackMode else WhiteSoft)
        .border(width = 1.dp, color = PrimaryGreen, shape = RoundedCornerShape(8.dp))
        .clickable { navController.navigate("chilliAI") }
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically) {
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
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isDarkTheme) BlackMode else GreenSoft)
    ) {
        TextBold(
            text = "Video Tutorial",
            sized = 12,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            textAlign = TextAlign.Start,
            colors = if (isSystemInDarkTheme()) White else BlackMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuVideoTutorial(
            title = "Penggunaan Chilli Vision",
            textDesc = "Silahkan memutar video berikut terlebih dahulu untuk memahami penggunaan Chilli Vision",
            icon = R.drawable.video_tutorial,
            isDarkTheme = isDarkTheme,
            context = context,
            link = "https://youtu.be/f446E-6fUk0"
        )
        MenuVideoTutorial(
            title = "Penggunaan Chilli AI",
            "Silahkan memutar video berikut apabila ingin mengetahui penggunaan Chilli AI",
            icon = R.drawable.video_tutorial,
            isDarkTheme = isDarkTheme,
            context = context,
            link = "https://youtu.be/EA7SZ01XJRw"
        )
    }
}

@Composable
private fun MenuVideoTutorial(
    title: String, textDesc: String, icon: Int, isDarkTheme: Boolean, context: Context, link: String
) {
    Row(modifier = Modifier
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
        verticalAlignment = Alignment.CenterVertically) {
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