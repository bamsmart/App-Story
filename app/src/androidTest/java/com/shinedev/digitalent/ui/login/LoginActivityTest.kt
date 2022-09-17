package com.shinedev.digitalent.ui.login

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.shinedev.digitalent.JsonConverter
import com.shinedev.digitalent.R
import com.shinedev.digitalent.common.EspressoIdlingResource
import com.shinedev.digitalent.data.remote.Retrofit
import com.shinedev.digitalent.ui.main.MainActivity
import com.shinedev.digitalent.ui.main.MainActivity.Companion.TOKEN
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    private val mockWebServer = MockWebServer()

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

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
    fun login_Error_Email() {
        val res = InstrumentationRegistry.getInstrumentation().targetContext.resources
        val textEmail = "bamsmart"
        onView(allOf(withId(R.id.et_input_text), isDescendantOfA(withId(R.id.ed_login_email))))
            .check(matches(isDisplayed()))
            .perform(ViewActions.typeText(textEmail))
        closeSoftKeyboard()

        onView(allOf(withId(R.id.tv_error), isDescendantOfA(withId(R.id.ed_login_email)))).check(
            matches(
                CoreMatchers.allOf(
                    withText(res.getString(R.string.error_format_email)),
                    isDisplayed()
                )
            )
        )
        onView(withId(R.id.btn_login))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun login_Error_Password() {
        val res = InstrumentationRegistry.getInstrumentation().targetContext.resources
        val textEmail = "123"
        onView(allOf(withId(R.id.et_input_text), isDescendantOfA(withId(R.id.ed_login_password))))
            .check(matches(isDisplayed()))
            .perform(ViewActions.typeText(textEmail))
        closeSoftKeyboard()

        onView(allOf(withId(R.id.tv_error), isDescendantOfA(withId(R.id.ed_login_password)))).check(
            matches(
                CoreMatchers.allOf(
                    withText(res.getString(R.string.error_length_password)),
                    isDisplayed()
                )
            )
        )
        onView(withId(R.id.btn_login))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun login_Failed() {
        val textEmail = "bamsmart.id@gmail.com"
        val textPassword = "1234567"

        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        onView(allOf(withId(R.id.et_input_text), isDescendantOfA(withId(R.id.ed_login_email))))
            .check(matches(isDisplayed()))
            .perform(ViewActions.typeText(textEmail))

        closeSoftKeyboard()

        onView(
            allOf(
                withId(R.id.et_input_text),
                isDescendantOfA(withId(R.id.ed_login_password))
            )
        )
            .check(matches(isDisplayed()))
            .perform(ViewActions.typeText(textPassword))

        closeSoftKeyboard()

        onView(withId(R.id.btn_login))
            .check(matches(isEnabled()))
            .perform(ViewActions.click())

        onView(withId(R.id.tv_snackbar_warning)).check(
            matches(
                AllOf.allOf(
                    isDisplayed(),
                    withText(
                        InstrumentationRegistry.getInstrumentation().targetContext.resources.getString(
                            R.string.text_account_error
                        )
                    )
                )
            )
        )
    }

    @Test
    fun login_Success() {
        val keyToken = "keyToken"
        val textEmail = "bamsmart.id@gmail.com"
        val textPassword = "1234567"

        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            LoginActivity::class.java
        ).putExtra(TOKEN, keyToken)

        val scenario = ActivityScenario.launch<LoginActivity>(intent)
        Intents.init()
        try {
            val mockResponse = MockResponse()
                .setResponseCode(200)
                .setBody(JsonConverter.readStringFromFile("mocked_login.json"))
            mockWebServer.enqueue(mockResponse)

            onView(allOf(withId(R.id.et_input_text), isDescendantOfA(withId(R.id.ed_login_email))))
                .check(matches(isDisplayed()))
                .perform(ViewActions.typeText(textEmail))

            closeSoftKeyboard()

            onView(
                allOf(
                    withId(R.id.et_input_text),
                    isDescendantOfA(withId(R.id.ed_login_password))
                )
            )
                .check(matches(isDisplayed()))
                .perform(ViewActions.typeText(textPassword))

            closeSoftKeyboard()

            onView(withId(R.id.btn_login))
                .check(matches(isEnabled()))
                .perform(ViewActions.click())

            Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
        } finally {
            Intents.release()
            scenario.close()
        }
    }
}