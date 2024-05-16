package com.example.myapplication

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testRevueValidation() {
        val validRevue = Revue("Valid Titre", "Valid Commentaire", "Valid Utilisateur", 4.0f, "Valid Image")
        val invalidRevue = Revue("", "", "", 6.0f, null)

        assertNotNull(validRevue)
        assertNotNull(invalidRevue)
    }


}