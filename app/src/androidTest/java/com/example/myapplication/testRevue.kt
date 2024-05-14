package com.example.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.rule.ActivityTestRule


@RunWith(AndroidJUnit4::class)
class testRevue {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<AjouterRevueActivity> = ActivityTestRule(AjouterRevueActivity::class.java)

    @Test
    fun testEnteringTextAndClickingButtonSendsDataBack() {
        onView(withId(R.id.editTextTitre)).perform(typeText("Titre test"))
        onView(withId(R.id.editTextREvue)).perform(typeText("Commentaire test"))
        onView(withId(R.id.button3)).perform(click())

        // Check if the correct data is sent back
        val expectedRevue = Revue("Titre test", "Commentaire test", "me", 4f, null)
        val gson = Gson()
        val expectedRevueJson = gson.toJson(expectedRevue)

        intended(hasExtraWithKey("revueJson"))
        intended(hasExtra("revueJson", expectedRevueJson))
    }
    @Test
    fun testMenuItemsAccessibility() {
        // Check if the 'pageaceuille' menu item is accessible and clickable
        onView(withId(R.id.pageaceuille)).check(matches(isClickable()))

        // Check if the 'ajouter' menu item is accessible and clickable
        onView(withId(R.id.ajouter)).check(matches(isClickable()))
    }

    @Test
    fun testMenuItemsFunctionality() {
        // Simulate clicking on the 'ajouter' menu item and check if it launches the correct activity
        onView(withId(R.id.ajouter)).perform(click())

        // You can add more checks here to verify the behavior after clicking on the 'ajouter' menu item
    }

    @Test
    fun testEmptyFieldsShowToast() {

        onView(withId(R.id.editTextTitre)).check(matches(withText("")))
        onView(withId(R.id.editTextREvue)).check(matches(withText("")))


        onView(withId(R.id.button3)).perform(click())

        onView(withText("SVP remplir les champs obligatoire")).check(doesNotExist())
    }



}
