package com.jeandealmeida_dev.itunesplayer.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jeandealmeida_dev.itunesplayer.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FromHome : NavigationTestBase() {

    @get:Rule
    override val composeTestRule = createAndroidComposeRule<MainActivity>()

    // region Home → Player

    @Test
    fun WHEN_track_is_tapped_THEN_player_screen_opens() {
        waitForHome()
        composeTestRule.onAllNodesWithText("Smells Like Teen Spirit")[0].performClick()
        composeTestRule.onNodeWithText("Now playing").assertIsDisplayed()
    }

    // endregion

    // region Home → Album

    @Test
    fun WHEN_view_album_is_tapped_from_action_sheet_THEN_album_screen_opens() {
        waitForHome()
        composeTestRule.onAllNodesWithContentDescription("More options")[0].performClick()
        composeTestRule.onNodeWithText("View album").performClick()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Songs").assertDoesNotExist()
    }

    // endregion
}
