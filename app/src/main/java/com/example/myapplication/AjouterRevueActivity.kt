package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAjouterRevueBinding
import com.google.gson.Gson
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.core.content.ContextCompat


class AjouterRevueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAjouterRevueBinding
    private var revue: Revue? = null
    private var uri: Uri? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Log.i("cameraLauncher", "Location de l'image: $uri")
            binding.photosoulier?.setImageURI(uri)
            revue?.image =uri.toString()
        } else {
            Log.i("cameraLauncher", "Could not save: $uri")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val revueJson = intent.getStringExtra("revue")
        val gson = Gson()
        revue = gson.fromJson(revueJson, Revue::class.java)
        super.onCreate(savedInstanceState)
        binding = ActivityAjouterRevueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Restore the state if there's a saved instance state
        savedInstanceState?.let {
            revue = it.getSerializable("revue") as? Revue
            binding.editTextTitre.setText(it.getString("titre", ""))
            binding.editTextREvue.setText(it.getString("reviewText", ""))
            uri = it.getParcelable("uri")
            uri?.let { binding.photosoulier.setImageURI(it) }
        }

        // If the revue object is not null, populate the EditText fields
        revue?.let {
            binding.editTextTitre.setText(it.titre)
            binding.editTextREvue.setText(it.commentaire)
            uri?.let { uri -> binding.photosoulier.setImageURI(uri) }
        }

        binding.buttonphoto.setOnClickListener {
            val fichier = createImageFile()
            // Check if permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Request camera permission if not granted
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                uri = FileProvider.getUriForFile(this,"com.example.myapplication.fileprovider",fichier)
                // Permission already granted, launch camera
                cameraLauncher.launch(uri)
            }

        }


        binding.button3.setOnClickListener {
            if (binding.editTextREvue.text.isBlank() || binding.editTextTitre.text.isBlank()) {
                Toast.makeText(applicationContext, "SVP remplir les champs obligatoire", Toast.LENGTH_SHORT).show()
            } else {
                val titre = binding.editTextTitre.text.toString()
                val reviewText = binding.editTextREvue.text.toString()
                val note = binding.spinnernote.selectedItem.toString().toFloat()
                val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val gsonuser = Gson()
                val json = sharedPreferences.getString("user", null)
                val user: Utilisateur = gsonuser.fromJson(json, Utilisateur::class.java)
                val newRevue = Revue(titre, reviewText, user.email, note, uri.toString())
                val gson = Gson()
                val soulierJson = gson.toJson(newRevue)
                intent.putExtra("revueJson",soulierJson)
                setResult(RESULT_OK,intent)
                finish()

            }
        }


    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission granted, launch camera
            launchCamera()
        } else {
            Toast.makeText(this, "La permission a été refusée", Toast.LENGTH_SHORT).show()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the revue object and other necessary data to restore the state later
        outState.putSerializable("revue", revue)
        outState.putString("titre", binding.editTextTitre.text.toString())
        outState.putString("reviewText", binding.editTextREvue.text.toString())
        outState.putParcelable("uri", uri)
    }






}
