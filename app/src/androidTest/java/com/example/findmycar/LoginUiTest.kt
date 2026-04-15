package com.example.findmycar

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.findmycar.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testLoginFieldsAreDisplayed() {
        // Check if Title is displayed
        onView(withId(R.id.textView_login_title))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.login_title)))

        // Check if Email field is displayed
        onView(withId(R.id.editText_username))
            .check(matches(isDisplayed()))
            .check(matches(withHint(R.string.username_hint)))

        // Check if Password field is displayed
        onView(withId(R.id.editText_password))
            .check(matches(isDisplayed()))
            .check(matches(withHint(R.string.password_hint)))

        // Check if Login button is displayed
        onView(withId(R.id.button_login))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.login_button)))
    }
}
