package com.example.myapplication


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
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
                } else {
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

        val soulierJson = intent.getStringExtra("soulier")
        val imageUriString = intent.getStringExtra("imageUri") // Retrieve the Uri as string
        val gson = Gson()
        soulier = gson.fromJson(soulierJson, Soulier::class.java)


        adapter = soulier.revues?.let { AdapteurRevues(this, it) }!!
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

    // Function to capture image from camera
    private fun captureImageFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.resolveActivity(packageManager)?.let {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
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
