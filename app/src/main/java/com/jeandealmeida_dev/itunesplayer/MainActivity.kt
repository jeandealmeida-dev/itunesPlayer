package com.jeandealmeida_dev.itunesplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.ui.home.HomeScreen
import com.jeandealmeida_dev.itunesplayer.ui.player.PlayerScreen
import com.jeandealmeida_dev.itunesplayer.ui.splash.SplashScreen
import com.jeandealmeida_dev.itunesplayer.ui.theme.ItunesPlayerTheme

private sealed class Screen {
    object Home : Screen()
    data class Player(val track: Track) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItunesPlayerTheme {
                var showSplash by remember { mutableStateOf(true) }
                var backStack by remember { mutableStateOf<List<Screen>>(listOf(Screen.Home)) }

                BackHandler(enabled = showSplash) {}

                if (showSplash) {
                    SplashScreen(onComplete = { showSplash = false })
                } else {
                    BackHandler(enabled = backStack.size > 1) {
                        backStack = backStack.dropLast(1)
                    }

                    when (val screen = backStack.last()) {
                        Screen.Home -> HomeScreen(
                            onTrackClick = { track ->
                                backStack = backStack + Screen.Player(track)
                            },
                        )
                        is Screen.Player -> PlayerScreen(
                            track = screen.track,
                            onBack = { backStack = backStack.dropLast(1) },
                        )
                    }
                }
            }
        }
    }
}
