package com.example.myapplication

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.content.res.Configuration
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityActiviterAjouterSoulierBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class ActiviterAjouterSoulier : AppCompatActivity() {
    private lateinit var binding: ActivityActiviterAjouterSoulierBinding
    private lateinit var soulier: Soulier
    private lateinit var adapter: AdapteurRevues

    // Initialisez ajouterRevueLauncher comme précédemment
    private val ajouterRevueLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val gson = Gson()
                val revueJson = it.getStringExtra("revueJson")
                val revue = gson.fromJson(revueJson, Revue::class.java)

                // Vérifiez si la revue récupérée existe déjà dans la liste
                val existingRevueIndex = soulier.revues?.indexOfFirst { it.Id == revue.Id }

                if (existingRevueIndex != -1) {
                    // Si la revue existe déjà, mettez à jour ses propriétés
                    if (existingRevueIndex != null) {
                        soulier.revues?.get(existingRevueIndex)?.apply {
                            titre = revue.titre
                            commentaire = revue.commentaire
                            note = revue.note
                            image = revue.image
                        }
                    }
                } else {
                    // Si la revue n'existe pas, ajoutez-la à la liste
                    soulier.revues?.add(revue)
                }
                it.removeExtra("revueJson")
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiviterAjouterSoulierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            // Restaurer l'état de l'activité
            soulier = (savedInstanceState.getSerializable("soulier") as Soulier?)!!
        } else {
            val soulierJson = intent.getStringExtra("soulier")
            val gson = Gson()
            soulier = gson.fromJson(soulierJson, Soulier::class.java)

            if (soulier.revues == null) {
                soulier.revues = mutableListOf()
            }
        }

        val imageButton = findViewById<ImageButton>(R.id.imageButton)




        adapter = soulier.revues?.let { AdapteurRevues(this, it) }!!
        binding.lstRevues.adapter = adapter
        binding.ratingBar.rating = soulier.note!!
        val intent = Intent(this, AjouterRevueActivity::class.java)

        binding.buttonajoutrevue.setOnClickListener {
            ajouterRevueLauncher.launch(intent)
        }
        binding.finir?.setOnClickListener {
            val gson = Gson()
            val returnIntent = Intent()
            returnIntent.putExtra("soulier", gson.toJson(soulier))
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.pageaceuille->{
                val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("user")
                editor.apply()

                val intent = Intent(
                    applicationContext,
                    Login::class.java
                )
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("EXIT", true)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Sauvegarder l'état de l'activité
        outState.putSerializable("soulier", soulier)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restaurer l'état de l'activité
        soulier = (savedInstanceState.getSerializable("soulier") as Soulier?)!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
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

