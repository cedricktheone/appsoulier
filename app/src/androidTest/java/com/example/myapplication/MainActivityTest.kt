package com.example.myapplication

import MainActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
       scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testMenuItemsAccessibility() {
        onView(withId(R.id.pageaceuille)).check(matches(isClickable()))
        onView(withId(R.id.ajouter)).check(matches(isClickable()))
    }

    @Test
    fun testMenuItemsFunctionality() {
        onView(withId(R.id.ajouter)).perform(click())
    }
}
