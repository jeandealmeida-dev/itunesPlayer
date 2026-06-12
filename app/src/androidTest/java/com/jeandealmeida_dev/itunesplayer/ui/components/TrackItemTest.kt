package com.jeandealmeida_dev.itunesplayer.ui.components

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
class TrackItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testTrack = aTrack(id = 1L, title = "Yesterday", artist = "The Beatles")

    @Test
    fun `GIVEN a track WHEN TrackItem is displayed THEN title is visible`() {
        composeTestRule.setContent {
            ItunesPlayerTheme {
                TrackItem(track = testTrack, showOptions = true, onClick = {})
            }
        }
        composeTestRule.onNodeWithText("Yesterday").assertIsDisplayed()
    }

    @Test
    fun `GIVEN a track WHEN TrackItem is displayed THEN artist name is visible`() {
        composeTestRule.setContent {
            ItunesPlayerTheme {
                TrackItem(track = testTrack, showOptions = true, onClick = {})
            }
        }
        composeTestRule.onNodeWithText("The Beatles").assertIsDisplayed()
    }

    @Test
    fun `GIVEN a track WHEN row is clicked THEN onClick callback is invoked`() {
        var clicked = false
        composeTestRule.setContent {
            ItunesPlayerTheme {
                TrackItem(track = testTrack, showOptions = false, onClick = { clicked = true })
            }
        }
        composeTestRule.onNodeWithText("Yesterday").performClick()
        assertTrue(clicked)
    }

    @Test
    fun `GIVEN showOptions is true WHEN TrackItem is displayed THEN more options button is visible`() {
        composeTestRule.setContent {
            ItunesPlayerTheme {
                TrackItem(track = testTrack, showOptions = true, onClick = {})
            }
        }
        composeTestRule.onNodeWithContentDescription("More options").assertIsDisplayed()
    }

    @Test
    fun `GIVEN showOptions is false WHEN TrackItem is displayed THEN more options button does not exist`() {
        composeTestRule.setContent {
            ItunesPlayerTheme {
                TrackItem(track = testTrack, showOptions = false, onClick = {})
            }
        }
        composeTestRule.onNodeWithContentDescription("More options").assertDoesNotExist()
    }

    @Test
    fun `GIVEN showOptions is true WHEN more options is clicked THEN onOptionsClick callback is invoked`() {
        var optionsClicked = false
        composeTestRule.setContent {
            ItunesPlayerTheme {
                TrackItem(
                    track = testTrack,
                    showOptions = true,
                    onClick = {},
                    onOptionsClick = { optionsClicked = true },
                )
            }
        }
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        assertTrue(optionsClicked)
    }
}
