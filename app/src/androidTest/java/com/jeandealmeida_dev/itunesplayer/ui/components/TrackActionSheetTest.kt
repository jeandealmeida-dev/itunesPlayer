package com.jeandealmeida_dev.itunesplayer.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jeandealmeida_dev.itunesplayer.aTrack
import com.jeandealmeida_dev.itunesplayer.ui.theme.ItunesPlayerTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackActionSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTrack = aTrack(
        id = 1L,
        title = "Bohemian Rhapsody",
        artist = "Queen",
    )

    private fun launchSheet(
        onViewAlbum: () -> Unit = {},
        onDismiss: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            ItunesPlayerTheme {
                TrackActionSheet(
                    track = testTrack,
                    onViewAlbum = onViewAlbum,
                    onDismiss = onDismiss,
                )
            }
        }
    }

    @Test
    fun WHEN_TrackActionSheet_is_shown_THEN_track_title_is_visible() {
        launchSheet()
        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertIsDisplayed()
    }

    @Test
    fun WHEN_TrackActionSheet_is_shown_THEN_artist_name_is_visible() {
        launchSheet()
        composeTestRule.onNodeWithText("Queen").assertIsDisplayed()
    }

    @Test
    fun WHEN_TrackActionSheet_is_shown_THEN_view_album_option_is_visible() {
        launchSheet()
        composeTestRule.onNodeWithText("View album").assertIsDisplayed()
    }

    @Test
    fun WHEN_view_album_is_tapped_THEN_onViewAlbum_is_invoked() {
        var albumClicked = false
        launchSheet(onViewAlbum = { albumClicked = true })
        composeTestRule.onNodeWithText("View album").performClick()
        assertTrue(albumClicked)
    }
}
