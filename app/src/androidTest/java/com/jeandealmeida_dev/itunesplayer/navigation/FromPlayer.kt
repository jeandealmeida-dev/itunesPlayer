package com.jeandealmeida_dev.itunesplayer.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
class FromPlayer : NavigationTestBase() {

    @get:Rule
    override val composeTestRule = createAndroidComposeRule<MainActivity>()

    // region Player → Home

    @Test
    fun WHEN_back_is_pressed_THEN_home_screen_is_shown() {
        navigateToPlayer()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Songs").assertIsDisplayed()
    }

    // endregion

    // region Player → Album

    @Test
    fun WHEN_album_link_is_tapped_THEN_album_screen_opens() {
        navigateToPlayer()
        composeTestRule.onNodeWithText("Nirvana").performClick()
        composeTestRule.waitUntil(timeoutMillis = 3_000) {
            composeTestRule.onAllNodesWithText("Now playing").fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    // endregion
}
