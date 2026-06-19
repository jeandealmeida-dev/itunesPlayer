package com.jeandealmeida_dev.itunesplayer.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
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
class TrackActionSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun launchSheet(
        onViewAlbum: () -> Unit = {},
        onDismiss: () -> Unit = {},
    ) {
        composeTestRule.setThemedContent {
            TrackActionSheet(
                track = NevermindFixture.smellsLikeTeenSpirit,
                onViewAlbum = onViewAlbum,
                onDismiss = onDismiss,
            )
        }
    }

    @Test
    fun WHEN_TrackActionSheet_is_shown_THEN_track_title_is_visible() {
        launchSheet()
        composeTestRule.onNodeWithText("Smells Like Teen Spirit").assertIsDisplayed()
    }

    @Test
    fun WHEN_TrackActionSheet_is_shown_THEN_artist_name_is_visible() {
        launchSheet()
        composeTestRule.onNodeWithText(NevermindFixture.ARTIST).assertIsDisplayed()
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
