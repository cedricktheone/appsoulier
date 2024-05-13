package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityAjouterRevueBinding
import com.google.gson.Gson

class AjouterRevueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAjouterRevueBinding
    private var revue: Revue? = null // Declare revue as nullable and initialize it to null
    override fun onCreate(savedInstanceState: Bundle?) {
        val revueJson = intent.getStringExtra("revue")
        val gson = Gson()
        revue = gson.fromJson(revueJson, Revue::class.java)
        super.onCreate(savedInstanceState)
        binding = ActivityAjouterRevueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (revue != null) {
            binding.editTextTitre.setText(revue!!.titre)
            binding.editTextREvue.setText(revue!!.commentaire)
        }

        binding.button3.setOnClickListener {

            if (binding.editTextREvue.text.isBlank() || binding.editTextTitre.text.isBlank()) {
                Toast.makeText(applicationContext, "SVP remplir les champs obligatoire", Toast.LENGTH_SHORT).show()

            } else {
                if (revue != null) {
                    // Modify attributes of existing revue
                    revue!!.titre = binding.editTextTitre.text.toString()
                    revue!!.commentaire = binding.editTextREvue.text.toString()
                } else {
                    // Create a new revue
                    revue = Revue(
                        binding.editTextTitre.text.toString(),
                        binding.editTextREvue.text.toString(),
                        "me",
                        4f,
                        null
                    )
                }

                val gson = Gson()
                val revueJson = gson.toJson(revue)

                val intent = Intent()
                intent.putExtra("revueJson", revueJson)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

    }
}
