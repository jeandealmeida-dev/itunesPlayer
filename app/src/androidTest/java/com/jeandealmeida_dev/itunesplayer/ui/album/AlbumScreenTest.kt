package com.jeandealmeida_dev.itunesplayer.ui.album

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
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

    @Test
    fun `GIVEN an album track WHEN AlbumScreen loads THEN album name is shown in the top bar`() {
        launchScreen()
        composeTestRule.onNodeWithText("Abbey Road").assertIsDisplayed()
    }

    @Test
    fun `GIVEN AlbumScreen is shown WHEN it loads THEN back button is visible`() {
        launchScreen()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun `GIVEN AlbumScreen is shown WHEN back button is clicked THEN onBack callback is invoked`() {
        var backPressed = false
        launchScreen(onBack = { backPressed = true })
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backPressed)
    }

    @Test
    fun `GIVEN album tracks WHEN AlbumScreen loads THEN track titles are visible`() {
        launchScreen()
        composeTestRule.onNodeWithText("Come Together").assertIsDisplayed()
        composeTestRule.onNodeWithText("Something").assertIsDisplayed()
    }

    @Test
    fun `GIVEN album tracks WHEN AlbumScreen loads THEN artist name is shown`() {
        launchScreen()
        composeTestRule.onNodeWithText("The Beatles").assertIsDisplayed()
    }

    @Test
    fun `GIVEN album tracks WHEN a track row is clicked THEN onTrackClick callback is invoked`() {
        var clickedTrack: Track? = null
        launchScreen(onTrackClick = { clickedTrack = it })
        composeTestRule.onNodeWithText("Come Together").performClick()
        assertNotNull(clickedTrack)
    }

    @Test
    fun `GIVEN no tracks WHEN AlbumScreen loads THEN track list is empty`() {
        launchScreen(tracks = emptyList())
        composeTestRule.onNodeWithText("Come Together").assertDoesNotExist()
    }
}
