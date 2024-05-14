package com.example.myapplication
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityFormUserBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter
import java.security.MessageDigest

class ActivityFormUser : AppCompatActivity() {
    private lateinit var binding:ActivityFormUserBinding
    private var userList = mutableListOf<Utilisateur>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userList = loadUserListFromJson().toMutableList()

        binding.button4.setOnClickListener {
            val email = binding.editTextText.text.toString()
            val password = binding.editTextText2.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                val existingUser = userList.find { it.email == email }
                if (existingUser != null) {
                    if (authenticateUser(password, existingUser)) {
                        saveUserToSharedPreferences(existingUser)
                        Toast.makeText(this, "User authenticated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Hash the password before using it
                    val hashedPassword = hashPassword(password)
                    val user = Utilisateur(email, hashedPassword)
                    userList.add(user)
                    saveUserListToJson(userList)
                    saveUserToSharedPreferences(user)
                    Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
        }
    }

    private fun saveUserToSharedPreferences(user: Utilisateur) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString("user", json)
        editor.apply()
    }

    fun saveUserListToJson(userList: List<Utilisateur>) {
        val gson = Gson()
        val userListJson = gson.toJson(userList)

        // Save JSON string to a file or use it as needed
        val file = File(filesDir, "user_list.json")
        FileWriter(file).use { writer ->
            writer.write(userListJson)
        }
    }

    fun loadUserListFromJson(): List<Utilisateur> {
        val file = File(filesDir, "user_list.json")
        return if (file.exists()) {
            val jsonString = file.readText()
            val listType = object : TypeToken<List<Utilisateur>>() {}.type
            Gson().fromJson(jsonString, listType)
        } else {
            emptyList()
        }
    }

    private fun hashPassword(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = digest.digest(password.toByteArray())
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            // Handle hashing error appropriately
            ""
        }
    }

    private fun checkPassword(inputPassword: String, storedPassword: String): Boolean {
        val hashedInputPassword = hashPassword(inputPassword)
        return hashedInputPassword == storedPassword
    }

    fun authenticateUser(inputPassword: String, user: Utilisateur): Boolean {
        return checkPassword(inputPassword, user.password)
    }
}