package com.jeandealmeida_dev.itunesplayer.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jeandealmeida_dev.itunesplayer.FakeTrackRepository
import com.jeandealmeida_dev.itunesplayer.aTrack
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.usecase.GetPagedTracksUseCase
import com.jeandealmeida_dev.itunesplayer.ui.theme.ItunesPlayerTheme
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val tracks = listOf(
        aTrack(id = 1L, title = "Yesterday", artist = "The Beatles"),
        aTrack(id = 2L, title = "Bohemian Rhapsody", artist = "Queen"),
    )

    private fun launchScreen(
        trackList: List<Track> = tracks,
        onTrackClick: (Track) -> Unit = {},
        onViewAlbum: (Track) -> Unit = {},
    ) {
        val viewModel = HomeViewModel(GetPagedTracksUseCase(FakeTrackRepository(tracks = trackList)))
        composeTestRule.setContent {
            ItunesPlayerTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onTrackClick = onTrackClick,
                    onViewAlbum = onViewAlbum,
                )
            }
        }
    }

    @Test
    fun WHEN_screen_loads_THEN_songs_title_is_shown() {
        launchScreen()
        composeTestRule.onNodeWithText("Songs").assertIsDisplayed()
    }

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
    fun WHEN_tracks_loaded_THEN_titles_are_visible() {
        launchScreen()
        composeTestRule.onNodeWithText("Yesterday").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertIsDisplayed()
    }

    @Test
    fun WHEN_tracks_loaded_THEN_artist_names_are_visible() {
        launchScreen()
        composeTestRule.onNodeWithText("The Beatles").assertIsDisplayed()
        composeTestRule.onNodeWithText("Queen").assertIsDisplayed()
    }

    @Test
    fun WHEN_no_tracks_exist_THEN_list_is_empty() {
        launchScreen(trackList = emptyList())
        composeTestRule.onNodeWithText("Yesterday").assertDoesNotExist()
    }

    @Test
    fun WHEN_user_types_in_search_THEN_input_is_reflected() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithText("Search").performTextInput("Queen")
        composeTestRule.onNodeWithText("Queen").assertIsDisplayed()
    }

    @Test
    fun `GIVEN search bar is open WHEN close button is clicked THEN search bar is hidden`() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithContentDescription("Close search").performClick()
        composeTestRule.onNodeWithText("Search").assertIsNotDisplayed()
    }

    @Test
    fun `GIVEN search bar is open WHEN close button is clicked THEN search icon reappears`() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.onNodeWithContentDescription("Close search").performClick()
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
    }

    @Test
    fun `GIVEN tracks are loaded WHEN a track row is clicked THEN onTrackClick callback is invoked`() {
        var clickedTrack: Track? = null
        launchScreen(onTrackClick = { clickedTrack = it })
        composeTestRule.onNodeWithText("Yesterday").performClick()
        assertNotNull(clickedTrack)
    }

    @Test
    fun `GIVEN a single track WHEN more options is clicked THEN action sheet appears`() {
        launchScreen(trackList = listOf(tracks[0]))
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("View album").assertIsDisplayed()
    }

    @Test
    fun `GIVEN action sheet is open WHEN view album is tapped THEN onViewAlbum callback is invoked`() {
        var viewAlbumTrack: Track? = null
        launchScreen(
            trackList = listOf(tracks[0]),
            onViewAlbum = { viewAlbumTrack = it },
        )
        composeTestRule.onNodeWithContentDescription("More options").performClick()
        composeTestRule.onNodeWithText("View album").performClick()
        assertNotNull(viewAlbumTrack)
    }
}
