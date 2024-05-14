package com.example.myapplication

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ActivityFormUserTest {

    @Test
    fun testCreateUser() {
        // Create a test user
        val testEmail = "test@example.com"
        val testPassword = "test123"
        val testUser = Utilisateur(testEmail, testPassword)

        // Get context and create activity
        val context = ApplicationProvider.getApplicationContext<Context>()
        val activity = ActivityFormUser()

        // Save test user
        activity.saveUserListToJson(listOf(testUser))

        // Load user list from JSON
        val userListJson = File(context.filesDir, "user_list.json").readText()
        val loadedUserList = activity.loadUserListFromJson(userListJson)

        // Check if the test user is in the loaded user list
        val foundUser = loadedUserList.find { it.email == testEmail }
        assertEquals("Test user not found in loaded user list", testUser, foundUser)
    }

    @Test
    fun testAuthenticateUser() {
        // Create a test user
        val testEmail = "test@example.com"
        val testPassword = "test123"
        val testUser = Utilisateur(testEmail, testPassword)

        // Get context and create activity
        val context = ApplicationProvider.getApplicationContext<Context>()
        val activity = ActivityFormUser()

        // Save test user
        activity.saveUserListToJson(listOf(testUser))

        // Load user list from JSON
        val userListJson = File(context.filesDir, "user_list.json").readText()
        val loadedUserList = activity.loadUserListFromJson(userListJson)

        // Check authentication
        val isAuthenticated = activity.authenticateUser(testPassword, testUser)
        assertEquals("Authentication failed", true, isAuthenticated)
    }
}
