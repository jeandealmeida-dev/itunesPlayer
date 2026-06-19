package com.jeandealmeida_dev.itunesplayer.ui.components

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
class TrackItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTrack = NevermindFixture.smellsLikeTeenSpirit

    @Test
    fun WHEN_TrackItem_is_displayed_THEN_title_is_visible() {
        composeTestRule.setThemedContent {
            TrackItem(track = testTrack, showOptions = true, onClick = {})
        }
        composeTestRule.onNodeWithText("Smells Like Teen Spirit").assertIsDisplayed()
    }

    @Test
    fun WHEN_TrackItem_is_displayed_THEN_artist_name_is_visible() {
        composeTestRule.setThemedContent {
            TrackItem(track = testTrack, showOptions = true, onClick = {})
        }
        composeTestRule.onNodeWithText(NevermindFixture.ARTIST).assertIsDisplayed()
    }

    @Test
    fun WHEN_track_row_is_clicked_THEN_onClick_is_invoked() {
        var clicked = false
        composeTestRule.setThemedContent {
            TrackItem(track = testTrack, showOptions = false, onClick = { clicked = true })
        }
        composeTestRule.onNodeWithText("Smells Like Teen Spirit").performClick()
        assertTrue(clicked)
    }

    @Test
    fun WHEN_showOptions_is_true_THEN_more_options_button_is_visible() {
        composeTestRule.setThemedContent {
            TrackItem(track = testTrack, showOptions = true, onClick = {})
        }
        composeTestRule.onNodeWithContentDescription("More options").assertIsDisplayed()
    }

    @Test
    fun WHEN_showOptions_is_false_THEN_more_options_button_does_not_exist() {
        composeTestRule.setThemedContent {
            TrackItem(track = testTrack, showOptions = false, onClick = {})
        }
        composeTestRule.onNodeWithContentDescription("More options").assertDoesNotExist()
    }

    @Test
    fun WHEN_more_options_is_clicked_THEN_onOptionsClick_is_invoked() {
        var optionsClicked = false
        composeTestRule.setThemedContent {
            TrackItem(
                track = testTrack,
                showOptions = true,
                onClick = {},
                onOptionsClick = { optionsClicked = true },
            )
        }
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        assertTrue(optionsClicked)
    }
}
