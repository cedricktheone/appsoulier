package com.example.myapplication

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.content.res.Configuration
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityActiviterAjouterSoulierBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class ActiviterAjouterSoulier : AppCompatActivity() {
    private lateinit var binding: ActivityActiviterAjouterSoulierBinding
    private lateinit var soulier: Soulier
    private lateinit var adapter: AdapteurRevues
    private lateinit var listeSoulier: MutableList<Soulier>

    private fun saveDataToStorage() {
        try {
            val gson = Gson()
            val jsonString = gson.toJson(listeSoulier)
            File("ListeData.json").writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadDataFromStorage() {
        try {
            val gson = Gson()
            val jsonString = File("ListeData.json").readText()
            val listType = object : TypeToken<MutableList<Soulier>>() {}.type
            val newlist: MutableList<Soulier> = gson.fromJson(jsonString, listType)
            listeSoulier.clear()
            listeSoulier.addAll(newlist)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Initialize ajouterRevueLauncher as before
    private val ajouterRevueLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val gson = Gson()
                val revueJson = it.getStringExtra("revueJson")
                val revue = gson.fromJson(revueJson, Revue::class.java)

                // Check if the retrieved revue already exists in the list
                val existingRevueIndex = soulier.revues?.indexOfFirst { it.Id == revue.Id }

                if (existingRevueIndex != -1) {
                    // If the revue already exists, update its properties
                    if (existingRevueIndex != null) {
                        soulier.revues?.get(existingRevueIndex)?.apply {
                            titre = revue.titre
                            commentaire = revue.commentaire
                            note = revue.note
                            image = revue.image
                        }
                    }
                } else {
                    // If the revue does not exist, add it to the list
                    soulier.revues?.add(revue)
                }
                it.removeExtra("revueJson")
                saveDataToStorage()
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiviterAjouterSoulierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if there's a saved instance state
        if (savedInstanceState != null) {
            soulier = savedInstanceState.getParcelable("soulier")!!
        } else {
            val soulierJson = intent.getStringExtra("soulier")
            val gson = Gson()
            soulier = gson.fromJson(soulierJson, Soulier::class.java)

            // Initialize revues list if it's null
            if (soulier.revues == null) {
                soulier.revues = mutableListOf()
            }
        }

        adapter = soulier.revues?.let { AdapteurRevues(this, it) }!!
        binding.lstRevues.adapter = adapter
        binding.ratingBar.rating = soulier.note!!
        val intent = Intent(this, AjouterRevueActivity::class.java)

        binding.buttonajoutrevue.setOnClickListener {
            ajouterRevueLauncher.launch(intent)
        }
        binding.finir?.setOnClickListener {
            val gson = Gson()
            // Finish the activity with an OK result
            val returnIntent = Intent()
            returnIntent.putExtra("updatedSoulier", gson.toJson(soulier))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        binding.lstRevues.setOnItemClickListener { parent, view, position, id ->
            val item = adapter.getItem(position) as Revue
            val gson = Gson()
            val revueJson = gson.toJson(item)
            intent.putExtra("revue", revueJson)
            ajouterRevueLauncher.launch(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        saveDataToStorage()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Image captured successfully
            val imageUri = data?.data
            if (imageUri != null) {
                // Pass the captured image URI to this activity
                val intent = Intent(this, ActiviterAjouterSoulier::class.java)
                intent.putExtra("imageUri", imageUri.toString())
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}
