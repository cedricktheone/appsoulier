package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityActiviterAjouterSoulierBinding
import com.google.gson.Gson

class ActiviterAjouterSoulier : AppCompatActivity() {
    private lateinit var binding: ActivityActiviterAjouterSoulierBinding
    private lateinit var soulier: Soulier
    private lateinit var adapter: AdapteurRevues // Declare adapter as a class-level variable

    // Initialize ajouterRevueLauncher as before
    val ajouterRevueLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val gson = Gson()
                val revueJson = it.getStringExtra("revueJson")
                val revue = gson.fromJson(revueJson, Revue::class.java)

                if (soulier.revues.contains(revue)) {
                    // Edit existing revue
                    val existingRevue = soulier.revues.find { it == revue }
                    existingRevue?.apply {
                        titre = revue.titre
                        commentaire = revue.commentaire
                        note = revue.note
                        image = revue.image
                    }
                } else {
                    // Add new revue
                    soulier.revues.add(revue)
                }

                adapter.notifyDataSetChanged() // Notify adapter of changes
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val soulierJson = intent.getStringExtra("soulier")
        val gson = Gson()
        soulier = gson.fromJson(soulierJson, Soulier::class.java)
        super.onCreate(savedInstanceState)
        binding = ActivityActiviterAjouterSoulierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AdapteurRevues(this, soulier.revues) // Initialize adapter here
        binding.lstRevues.adapter = adapter
        binding.ratingBar.rating = soulier.note

        binding.buttonajoutrevue.setOnClickListener {
            val intent = Intent(this, AjouterRevueActivity::class.java)
            ajouterRevueLauncher.launch(intent)
        }

        binding.lstRevues.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as Revue
            val RevueJson = gson.toJson(item)

            intent.putExtra("revue", RevueJson)
        }
    }
}

