package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityActiviterAjouterSoulierBinding
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson

class ActiviterAjouterSoulier : AppCompatActivity() {
    private lateinit var binding:ActivityActiviterAjouterSoulierBinding
    val ajouterRevueLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val data: Intent? = result.data
            data?.let {
                val titre = it.getStringExtra("titre")
                val commentaire = it.getStringExtra("commentaire")
                val utilisateur = it.getStringExtra("utilisateur")
                val note = it.getFloatExtra("note", 0f)
                val image = it.getIntExtra("image", 0)

                // Create a Revue object with the retrieved data
                val revue = Revue(titre ?: "", commentaire ?: "", utilisateur ?: "", note, image)

                // Perform any actions with the newly created Revue object
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiviterAjouterSoulierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val soulierJson = intent.getStringExtra("soulier")
        val gson = Gson()
        val soulier: Soulier = gson.fromJson(soulierJson, Soulier::class.java)
        val adapter = AdapteurRevues(this, soulier.revues)
        println(soulier.revues)
        binding.lstRevues?.adapter = adapter
        binding.ratingBar.rating = soulier.note





        binding.buttonajoutrevue.setOnClickListener {
            val intent = Intent(this, AjouterRevueActivity::class.java)
            ajouterRevueLauncher.launch(intent)
        }
    }

}