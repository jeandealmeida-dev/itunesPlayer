package com.jeandealmeida_dev.itunesplayer.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jeandealmeida_dev.itunesplayer.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onComplete: () -> Unit) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 700),
                    repeatMode = RepeatMode.Reverse,
                )
            )
        }
        delay(1500)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .paint(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentScale = ContentScale.FillBounds,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(28.dp))
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                )
        )
    }
}
