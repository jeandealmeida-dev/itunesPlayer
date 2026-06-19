package com.jeandealmeida_dev.itunesplayer.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.jeandealmeida_dev.itunesplayer.MainActivity
import com.jeandealmeida_dev.itunesplayer.data.network.NetworkModule
import com.jeandealmeida_dev.itunesplayer.data.remote.ItunesService
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.BeforeClass

abstract class NavigationTestBase {

    abstract val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

    fun waitForHome() {
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodesWithText("Smells Like Teen Spirit")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun navigateToPlayer() {
        waitForHome()
        composeTestRule.onAllNodesWithText("Smells Like Teen Spirit")[0].performClick()
        composeTestRule.onNodeWithText("Now playing").assertIsDisplayed()
    }

    fun navigateToAlbum() {
        waitForHome()
        composeTestRule.onAllNodesWithContentDescription("More options")[0].performClick()
        composeTestRule.onNodeWithText("View album").performClick()
        composeTestRule.waitUntil(timeoutMillis = 3_000) {
            composeTestRule.onAllNodesWithText("Smells Like Teen Spirit").fetchSemanticsNodes().isNotEmpty()
        }
    }

    companion object {
        private lateinit var mockWebServer: MockWebServer

        @BeforeClass
        @JvmStatic
        fun setUpMockServer() {
            val tracksJson = InstrumentationRegistry.getInstrumentation().context
                .assets.open("nevermind.json").bufferedReader().readText()
            mockWebServer = MockWebServer()
            mockWebServer.start()
            mockWebServer.dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse =
                    MockResponse().setResponseCode(200).setBody(tracksJson)
            }
            NetworkModule.itunesService = NetworkModule.createService(mockWebServer.url("/").toString())
        }

        @AfterClass
        @JvmStatic
        fun tearDownMockServer() {
            NetworkModule.itunesService = NetworkModule.createService(ItunesService.BASE_URL)
            mockWebServer.shutdown()
        }
    }
}
