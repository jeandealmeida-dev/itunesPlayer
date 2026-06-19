package com.jeandealmeida_dev.itunesplayer.ui.player

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jeandealmeida_dev.itunesplayer.NevermindFixture
import com.jeandealmeida_dev.itunesplayer.setThemedContent
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun launchScreen(onBack: () -> Unit = {}, onAlbumClick: () -> Unit = {}) {
        composeTestRule.setThemedContent {
            PlayerScreen(
                track = NevermindFixture.smellsLikeTeenSpirit,
                onBack = onBack,
                onAlbumClick = onAlbumClick,
            )
        }
    }

    // region Top bar

    @Test
    fun WHEN_player_loads_THEN_now_playing_title_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithText("Now playing").assertIsDisplayed()
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

    // endregion

    // region Track info

    @Test
    fun WHEN_player_loads_THEN_track_title_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithText("Smells Like Teen Spirit").assertIsDisplayed()
    }

    @Test
    fun WHEN_player_loads_THEN_artist_name_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithText(NevermindFixture.ARTIST).assertIsDisplayed()
    }

    @Test
    fun WHEN_track_title_is_tapped_THEN_onAlbumClick_is_invoked() {
        var albumClicked = false
        launchScreen(onAlbumClick = { albumClicked = true })
        composeTestRule.onNodeWithText("Smells Like Teen Spirit").performClick()
        assertTrue(albumClicked)
    }

    @Test
    fun WHEN_artist_name_is_tapped_THEN_onAlbumClick_is_invoked() {
        var albumClicked = false
        launchScreen(onAlbumClick = { albumClicked = true })
        composeTestRule.onNodeWithText(NevermindFixture.ARTIST).performClick()
        assertTrue(albumClicked)
    }

    // endregion

    // region Playback controls

    @Test
    fun WHEN_player_loads_THEN_play_button_is_visible() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Play").assertIsDisplayed()
    }

    @Test
    fun WHEN_player_loads_THEN_repeat_button_is_visible() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Repeat").assertIsDisplayed()
    }

    // endregion

    // region Repeat

    @Test
    fun WHEN_player_loads_THEN_repeat_badge_is_not_shown() {
        launchScreen()
        composeTestRule.onNodeWithText("1").assertDoesNotExist()
    }

    @Test
    fun WHEN_repeat_button_is_clicked_THEN_repeat_badge_appears() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Repeat").performClick()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun WHEN_repeat_is_active_and_button_is_clicked_again_THEN_repeat_badge_disappears() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Repeat").performClick()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Repeat").performClick()
        composeTestRule.onNodeWithText("1").assertDoesNotExist()
    }

    // endregion

    // region Track action sheet

    @Test
    fun WHEN_more_options_is_clicked_THEN_view_album_option_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("View album").assertIsDisplayed()
    }

    @Test
    fun WHEN_view_album_is_tapped_THEN_onAlbumClick_is_invoked() {
        var albumClicked = false
        launchScreen(onAlbumClick = { albumClicked = true })
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("View album").performClick()
        assertTrue(albumClicked)
    }

    // endregion
}
