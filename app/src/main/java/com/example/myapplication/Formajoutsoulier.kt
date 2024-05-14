package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityFormajoutsoulierBinding

class Formajoutsoulier : AppCompatActivity() {
    private lateinit var binding: ActivityFormajoutsoulierBinding
    private var uri: Uri? = null
    private var soulier: Soulier? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Use the uri variable declared outside this block
            Log.i("cameraLauncher", "Location de l'image: $uri")

            // Set the image URI to the Revue object
            binding.imageView4.setImageURI(uri)
            soulier?.image =uri.toString()

            // Update the ImageView in the layout with the captured image


        } else {
            Log.i("cameraLauncher", "Could not save: $uri")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormajoutsoulierBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}