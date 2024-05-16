package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityFormajoutsoulierBinding
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Formajoutsoulier : AppCompatActivity() {
    private lateinit var binding: ActivityFormajoutsoulierBinding
    private var uri: Uri? = null
    private var soulier: Soulier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormajoutsoulierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restaurer l'état des champs EditText si savedInstanceState n'est pas nul
        if (savedInstanceState != null) {
            binding.editTextprix.setText(savedInstanceState.getString("title"))
            binding.textViewprix.setText(savedInstanceState.getString("description"))
        }

        binding.button5.setOnClickListener {
            val fichier = createImageFile()
            // Vérifier si la permission est accordée
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Demander l'autorisation de la caméra si elle n'est pas accordée
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                uri = FileProvider.getUriForFile(this,"com.example.myapplication.fileprovider",fichier)
                // Autorisation déjà accordée, lancer la caméra
            }
            cameraLauncher.launch(uri)
        }


        binding.buttonsoumission.setOnClickListener {
            // Récupérer les valeurs des éléments de l'interface utilisateur
            val nom = binding.editnom.text.toString()
            val prix = binding.editTextprix.text.toString().toFloatOrNull() ?: 0f
            val imageUri = uri.toString()
            val soulier = Soulier(nom, prix, imageUri)
            val gson = Gson()
            val soulierJson = gson.toJson(soulier)
            val resultIntent = Intent()
            resultIntent.putExtra("soulier", soulierJson)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission accordée, lancer la caméra
            launchCamera()
        } else {
            Toast.makeText(this, "La permission a été refusée", Toast.LENGTH_SHORT).show()
        }
    }
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Log.i("cameraLauncher", "Emplacement de l'image : $uri")
            binding.imageViewphotosoulier.setImageURI(uri)
            soulier?.image = uri.toString()
        } else {
            Log.i("cameraLauncher", "Impossible de sauvegarder : $uri")
        }
    }

    private fun launchCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

        photoFile?.let {
            uri = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider", it)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            cameraLauncher.launch(uri)
        }
    }
    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_$timestamp", ".jpg", storageDir)
    }
}
