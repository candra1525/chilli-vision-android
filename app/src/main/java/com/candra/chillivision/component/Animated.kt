package com.candra.chillivision.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.candra.chillivision.R

@Composable
fun AnimatedLoading(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_2))
    val animatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        animatable.animate(
            composition,
            iterations = LottieConstants.IterateForever,
            speed = 1.5f,
        )
    }

    LottieAnimation(
        composition = composition,
        progress = animatable.progress,
        modifier = modifier,
    )
}


@Composable
fun AnimatedLoadingChat(modifier: Modifier = Modifier) {
    val animasiLoad by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.loading_chat
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