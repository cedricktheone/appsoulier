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
    private lateinit var binding: ActivityFormUserBinding
    private var userList = mutableListOf<Utilisateur>()
    private lateinit var emailText: String
    private lateinit var passwordText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userList = loadUserListFromJson().toMutableList()

        if (savedInstanceState != null) {
            emailText = savedInstanceState.getString("emailText", "")
            passwordText = savedInstanceState.getString("passwordText", "")
            binding.editTextText.setText(emailText)
            binding.editTextText2.setText(passwordText)
        }

        binding.button4.setOnClickListener {
            val email = binding.editTextText.text.toString()
            val password = binding.editTextText2.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Veuillez remplir les deux champs", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                val existingUser = userList.find { it.email == email }
                if (existingUser != null) {
                    if (authenticateUser(password, existingUser)) {
                        saveUserToSharedPreferences(existingUser)
                        Toast.makeText(this, "Utilisateur authentifié", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Hasher le mot de passe avant de l'utiliser
                    val hashedPassword = hashPassword(password)
                    val user = Utilisateur(email, hashedPassword)
                    userList.add(user)
                    saveUserListToJson(userList)
                    saveUserToSharedPreferences(user)
                    Toast.makeText(this, "Utilisateur créé avec succès", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        emailText = binding.editTextText.text.toString()
        passwordText = binding.editTextText2.text.toString()
        outState.putString("emailText", emailText)
        outState.putString("passwordText", passwordText)
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

        // Enregistrer la chaîne JSON dans un fichier ou l'utiliser selon les besoins
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
