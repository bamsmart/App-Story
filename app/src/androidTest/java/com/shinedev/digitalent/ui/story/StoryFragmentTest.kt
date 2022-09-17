package com.shinedev.digitalent.ui.story

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.shinedev.digitalent.JsonConverter
import com.shinedev.digitalent.R
import com.shinedev.digitalent.common.EspressoIdlingResource
import com.shinedev.digitalent.data.remote.Retrofit
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class StoryFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        Retrofit.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getListStory_Error() {
        launchFragmentInContainer<StoryFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun getListStory_Empty() {
        launchFragmentInContainer<StoryFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("empty_story.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun getListStory_Success() {
        launchFragmentInContainer<StoryFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mocked_story.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_story))
            .check(matches(isDisplayed()))

        onView(withText("Bjorka"))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_story))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Bams"))
                )
            )
    }
}