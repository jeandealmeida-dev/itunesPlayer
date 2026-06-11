package com.jeandealmeida_dev.itunesplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jeandealmeida_dev.itunesplayer.ui.splash.SplashScreen
import com.jeandealmeida_dev.itunesplayer.ui.theme.ItunesPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ItunesPlayerTheme {
                var showSplash by remember { mutableStateOf(true) }

                BackHandler(enabled = showSplash) {}

                if (showSplash) {
                    SplashScreen(onComplete = { showSplash = false })
                } else {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Home")
                        }
                    }
                }
            }
        }
    }
}
