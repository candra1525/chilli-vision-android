package com.candra.chillivision.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.candra.chillivision.R

@Composable
fun ImageNotFound(modifier: Modifier = Modifier) {
    val animasiLoad by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.image_not_found)
    )

    val animasiLoadProgress by animateLottieCompositionAsState(
        composition = animasiLoad,
        iterations = 1,
        isPlaying = true,
        restartOnPlay = false
    )

    LottieAnimation(
        composition = animasiLoad,
        progress = animasiLoadProgress,
        modifier = modifier
    )
}
