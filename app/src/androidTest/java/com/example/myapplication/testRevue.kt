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
    fun testEmptyFieldsShowToast() {
        // Ensure the EditText fields are empty
        onView(withId(R.id.editTextTitre)).check(matches(withText("")))
        onView(withId(R.id.editTextREvue)).check(matches(withText("")))

        // Perform click on the button
        onView(withId(R.id.button3)).perform(click())

        // Verify that no further action is taken (e.g., toast shown, activity launched)
        onView(withText("SVP remplir les champs obligatoire")).check(doesNotExist())
    }



}
