package com.jeandealmeida_dev.itunesplayer.ui.player

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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
class PlayerScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTrack = aTrack(
        id = 1L,
        title = "Bohemian Rhapsody",
        artist = "Queen",
    )

    private fun launchScreen(onBack: () -> Unit = {}, onAlbumClick: () -> Unit = {}) {
        composeTestRule.setContent {
            ItunesPlayerTheme {
                PlayerScreen(track = testTrack, onBack = onBack, onAlbumClick = onAlbumClick)
            }
        }
    }

    @Test
    fun WHEN_player_loads_THEN_now_playing_title_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithText("Now playing").assertIsDisplayed()
    }

    @Test
    fun WHEN_player_loads_THEN_track_title_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertIsDisplayed()
    }

    @Test
    fun WHEN_player_loads_THEN_artist_name_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithText("Queen").assertIsDisplayed()
    }

    @Test
    fun WHEN_player_loads_THEN_back_button_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun WHEN_back_button_clicked_THEN_onBack_is_called() {
        var backPressed = false
        launchScreen(onBack = { backPressed = true })
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backPressed)
    }

    @Test
    fun WHEN_player_loads_THEN_more_options_button_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("More options").assertIsDisplayed()
    }

    @Test
    fun `GIVEN player is loaded THEN play button is visible`() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Play").assertIsDisplayed()
    }

    @Test
    fun `GIVEN player is loaded THEN repeat button is visible`() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Repeat").assertIsDisplayed()
    }

    @Test
    fun `GIVEN player is loaded WHEN more options is clicked THEN view album option is shown`() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("View album").assertIsDisplayed()
    }

    @Test
    fun `GIVEN action sheet is open WHEN view album is tapped THEN onAlbumClick callback is invoked`() {
        var albumClicked = false
        launchScreen(onAlbumClick = { albumClicked = true })
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("View album").performClick()
        assertTrue(albumClicked)
    }

    @Test
    fun `GIVEN player is loaded WHEN track title is tapped THEN onAlbumClick callback is invoked`() {
        var albumClicked = false
        launchScreen(onAlbumClick = { albumClicked = true })
        composeTestRule.onNodeWithText("Bohemian Rhapsody").performClick()
        assertTrue(albumClicked)
    }

    @Test
    fun `GIVEN player is loaded WHEN artist name is tapped THEN onAlbumClick callback is invoked`() {
        var albumClicked = false
        launchScreen(onAlbumClick = { albumClicked = true })
        composeTestRule.onNodeWithText("Queen").performClick()
        assertTrue(albumClicked)
    }
}
