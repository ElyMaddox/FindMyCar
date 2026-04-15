package com.example.findmycar

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.findmycar.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testHomeDashboardIsDefined() {
        // 1. Bypass Login
        onView(withId(R.id.editText_username)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editText_password)).perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.button_login)).perform(click())

        // 2. Verify Dashboard Title using ID
        onView(withId(R.id.textView_home_title))
            .check(matches(isDisplayed()))
            .check(matches(withText("Dashboard")))

        // 3. Verify Interactive Search button
        onView(withId(R.id.button_interactive_search))
            .check(matches(isDisplayed()))
    }
}
