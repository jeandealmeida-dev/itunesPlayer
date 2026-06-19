package com.jeandealmeida_dev.itunesplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.jeandealmeida_dev.itunesplayer.ui.theme.ItunesPlayerTheme

fun ComposeContentTestRule.setThemedContent(content: @Composable () -> Unit) {
    setContent { ItunesPlayerTheme { content() } }
}
