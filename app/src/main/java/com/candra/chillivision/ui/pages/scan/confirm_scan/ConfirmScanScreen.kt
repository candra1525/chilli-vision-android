package com.candra.chillivision.ui.pages.scan.confirm_scan

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.candra.chillivision.R
import com.candra.chillivision.component.ButtonCustomColorWithIcon
import com.candra.chillivision.component.HeaderComponent
import com.candra.chillivision.component.TextBold
import com.candra.chillivision.component.dashedBorder
import com.candra.chillivision.ui.theme.PrimaryGreen

@Composable
fun ConfirmScanScreen(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isLoading by remember { mutableStateOf(false) }
    // Ambil Gambar dari Intent
//    val navBackStackEntry = navController.currentBackStackEntry
//    val imageUri = navBackStackEntry?.arguments?.getString("imageUri")
    val imageUri by remember {
        mutableStateOf(navController.currentBackStackEntry?.arguments?.getString("imageUri"))
    }


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (!isLoading) {
            HeaderComponent("Konfirmasi Deteksi", modifier, navController)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {

            if (!isLoading) {
                Column(modifier = Modifier) {
                    ContentDetection(imageUri)

                    Spacer(modifier = Modifier.padding(16.dp))

                    ButtonCustomColorWithIcon(
                        onClick = {
                            //                        navController.navigate("analysis")
                            isLoading = true
                        },
                        text = "Deteksi",
                        color = PrimaryGreen,
                        icon = Icons.Filled.Search,
                        modifier = Modifier.padding(32.dp, 0.dp)
                    )
                }
            }

            if (isLoading) {
                Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedLoading(modifier = Modifier.size(150.dp))
                    TextBold(text = "Gambar sedang di deteksi", sized = 14)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextBold(text = "Mohon Menunggu ...", sized = 14)
                }

            }

        }
    }

}

@Composable
fun ContentDetection(imageUri: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(32.dp, 0.dp)
            .dashedBorder(2.dp, 8.dp, PrimaryGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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


@Composable
fun AnimatedLoading(modifier: Modifier = Modifier) {
    val animasiLoad by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.loading_animation_for_analysis
        )
    )

    val animasiLoadProgress by animateLottieCompositionAsState(
        animasiLoad,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    LottieAnimation(
        composition = animasiLoad,
        progress = animasiLoadProgress,
        modifier = modifier
    )
}