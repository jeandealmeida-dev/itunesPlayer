package com.jeandealmeida_dev.itunesplayer.ui.album

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jeandealmeida_dev.itunesplayer.FakeTrackRepository
import com.jeandealmeida_dev.itunesplayer.aTrack
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.usecase.GetAlbumTracksUseCase
import com.jeandealmeida_dev.itunesplayer.ui.theme.ItunesPlayerTheme
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val headerTrack = aTrack(
        id = 1L,
        title = "Come Together",
        artist = "The Beatles",
        album = "Abbey Road",
        collectionId = 123L,
    )

    private val albumTracks = listOf(
        aTrack(id = 1L, title = "Come Together", artist = "The Beatles"),
        aTrack(id = 2L, title = "Something", artist = "The Beatles"),
    )

    private fun launchScreen(
        tracks: List<Track> = albumTracks,
        onBack: () -> Unit = {},
        onTrackClick: (Track) -> Unit = {},
    ) {
        val viewModel = AlbumViewModel(GetAlbumTracksUseCase(FakeTrackRepository(tracks = tracks)))
        composeTestRule.setContent {
            ItunesPlayerTheme {
                AlbumScreen(
                    track = headerTrack,
                    onBack = onBack,
                    onTrackClick = onTrackClick,
                    viewModel = viewModel,
                )
            }
        }
    }

    // region Top bar

    @Test
    fun WHEN_AlbumScreen_loads_THEN_album_name_is_shown() {
        launchScreen()
        composeTestRule.onAllNodesWithText("Abbey Road")[0].assertIsDisplayed()
    }

    @Test
    fun WHEN_AlbumScreen_loads_THEN_back_button_is_visible() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun WHEN_back_button_is_clicked_THEN_onBack_is_invoked() {
        var backPressed = false
        launchScreen(onBack = { backPressed = true })
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backPressed)
    }

    // endregion

    // region Track list

    @Test
    fun WHEN_AlbumScreen_loads_THEN_track_titles_are_visible() {
        launchScreen()
        composeTestRule.onNodeWithText("Come Together").assertIsDisplayed()
        composeTestRule.onNodeWithText("Something").assertIsDisplayed()
    }

    @Test
    fun WHEN_AlbumScreen_loads_THEN_artist_name_is_shown() {
        launchScreen()
        composeTestRule.onAllNodesWithText("The Beatles")[0].assertIsDisplayed()
    }

    @Test
    fun WHEN_track_row_is_clicked_THEN_onTrackClick_is_invoked() {
        var clickedTrack: Track? = null
        launchScreen(onTrackClick = { clickedTrack = it })
        composeTestRule.onNodeWithText("Come Together").performClick()
        assertNotNull(clickedTrack)
    }

    @Test
    fun WHEN_no_tracks_loaded_THEN_track_list_is_empty() {
        launchScreen(tracks = emptyList())
        composeTestRule.onNodeWithText("Come Together").assertDoesNotExist()
    }

    // endregion
}
