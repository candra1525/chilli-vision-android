package com.candra.chillivision.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.candra.chillivision.R

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