package com.jeandealmeida_dev.itunesplayer.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jeandealmeida_dev.itunesplayer.FakeTrackRepository
import com.jeandealmeida_dev.itunesplayer.NevermindFixture
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.usecase.GetPagedTracksUseCase
import com.jeandealmeida_dev.itunesplayer.setThemedContent
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun launchScreen(
        trackList: List<Track> = NevermindFixture.allTracks,
        onTrackClick: (Track) -> Unit = {},
        onViewAlbum: (Track) -> Unit = {},
    ) {
        val viewModel = HomeViewModel(GetPagedTracksUseCase(FakeTrackRepository(tracks = trackList)))
        composeTestRule.setThemedContent {
            HomeScreen(
                viewModel = viewModel,
                onTrackClick = onTrackClick,
                onViewAlbum = onViewAlbum,
            )
        }
    }

    // region Search bar

    @Test
    fun WHEN_screen_loads_THEN_search_bar_is_hidden() {
        launchScreen()
        composeTestRule.onNodeWithText("Search").assertIsNotDisplayed()
    }

    @Test
    fun WHEN_search_icon_clicked_THEN_search_bar_appears() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }

    @Test
    fun WHEN_search_icon_clicked_THEN_search_icon_is_hidden() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithContentDescription("Search").assertIsNotDisplayed()
    }

    @Test
    fun WHEN_user_types_in_search_THEN_input_is_reflected() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithText("Search").performTextInput("Nirvana")
        composeTestRule.onNode(hasSetTextAction() and hasText("Nirvana")).assertIsDisplayed()
    }

    @Test
    fun WHEN_close_button_is_clicked_THEN_search_bar_is_hidden() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithContentDescription("Close search").performClick()
        composeTestRule.onNodeWithText("Search").assertIsNotDisplayed()
    }

    @Test
    fun WHEN_close_button_is_clicked_THEN_search_icon_reappears() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithContentDescription("Close search").performClick()
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
    }

    // endregion

    // region Track list

    @Test
    fun WHEN_screen_loads_THEN_songs_title_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithText("Songs").assertIsDisplayed()
    }

    @Test
    fun WHEN_tracks_loaded_THEN_titles_are_visible() {
        launchScreen()
        composeTestRule.onNodeWithText("Smells Like Teen Spirit").assertIsDisplayed()
        composeTestRule.onNodeWithText("In Bloom").assertIsDisplayed()
    }

    @Test
    fun WHEN_tracks_loaded_THEN_artist_names_are_visible() {
        launchScreen()
        composeTestRule.onAllNodesWithText(NevermindFixture.ARTIST)[0].assertIsDisplayed()
    }

    @Test
    fun WHEN_no_tracks_exist_THEN_list_is_empty() {
        launchScreen(trackList = emptyList())
        composeTestRule.onNodeWithText("Smells Like Teen Spirit").assertDoesNotExist()
    }

    @Test
    fun WHEN_track_row_is_clicked_THEN_onTrackClick_is_invoked() {
        var clickedTrack: Track? = null
        launchScreen(onTrackClick = { clickedTrack = it })
        composeTestRule.onNodeWithText("Smells Like Teen Spirit").performClick()
        assertNotNull(clickedTrack)
    }

    // endregion

    // region Track action sheet

    @Test
    fun WHEN_more_options_is_clicked_THEN_action_sheet_appears() {
        launchScreen(trackList = listOf(NevermindFixture.smellsLikeTeenSpirit))
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("View album").assertIsDisplayed()
    }

    @Test
    fun WHEN_view_album_is_tapped_THEN_onViewAlbum_is_invoked() {
        var viewAlbumTrack: Track? = null
        launchScreen(
            trackList = listOf(NevermindFixture.smellsLikeTeenSpirit),
            onViewAlbum = { viewAlbumTrack = it },
        )
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("View album").performClick()
        assertNotNull(viewAlbumTrack)
    }

    // endregion
}
