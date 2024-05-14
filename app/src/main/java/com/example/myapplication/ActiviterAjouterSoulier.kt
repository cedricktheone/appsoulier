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
    private lateinit var adapter: AdapteurRevues

    // Initialize ajouterRevueLauncher as before
    val ajouterRevueLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val gson = Gson()
                val revueJson = it.getStringExtra("revueJson")
                val revue = gson.fromJson(revueJson, Revue::class.java)


                val existingRevue = soulier.revues?.find { it.Id == revue.Id }
                if (existingRevue != null) {
                    existingRevue.titre = revue.titre
                    existingRevue.commentaire = revue.commentaire
                    existingRevue.note = revue.note
                    existingRevue.image = revue.image
                }
                else{

                    soulier.revues?.add(revue)
                }
                adapter.notifyDataSetChanged()
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

        adapter = soulier.revues?.let { AdapteurRevues(this, it) }!! // Initialize adapter here
        binding.lstRevues.adapter = adapter
        binding.ratingBar.rating = soulier.note!!
        val intent = Intent(this, AjouterRevueActivity::class.java)


        binding.buttonajoutrevue.setOnClickListener {
            ajouterRevueLauncher.launch(intent)
        }

        binding.lstRevues.setOnItemClickListener { parent, view, position, id ->
            val item = adapter.getItem(position) as Revue
            val revueJson = gson.toJson(item)
            intent.putExtra("revue", revueJson)
            ajouterRevueLauncher.launch(intent)
        }

    }
}

