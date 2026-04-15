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
class NavigationUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testNavigationFromLoginToHome() {
        // 1. Enter bypass credentials
        onView(withId(R.id.editText_username)).perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editText_password)).perform(typeText("password123"), closeSoftKeyboard())

        // 2. Click Login
        onView(withId(R.id.button_login)).perform(click())

        // 3. Verify we reached the Home screen by checking for the Dashboard title using ID
        onView(withId(R.id.textView_home_title))
            .check(matches(isDisplayed()))
            .check(matches(withText("Dashboard")))
        
        // 4. Verify that we can then navigate to Profile
        onView(withId(R.id.button_go_to_profile)).perform(click())
        
        // 5. Verify Profile title is displayed
        onView(withId(R.id.textView_profile_title)).check(matches(isDisplayed()))
    }
}
