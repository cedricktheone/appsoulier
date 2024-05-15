package com.example.myapplication

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.myapplication", appContext.packageName)
    }
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(ActivityFormUser::class.java)


    fun testButtonClick() {
        // Attendez que l'activité soit lancée et prête
        ActivityScenario.launch(ActivityFormUser::class.java)

        // Remplissez les champs EditText avec du texte
        onView(withId(R.id.editTextText)).perform(typeText("example@email.com"))
        onView(withId(R.id.editTextText2)).perform(typeText("password"))

        // Cliquez sur le bouton4
        onView(withId(R.id.button4)).perform(click())
    }
}