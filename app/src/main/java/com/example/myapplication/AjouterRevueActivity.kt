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
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


class AjouterRevueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAjouterRevueBinding
    private var revue: Revue? = null
    private var uri: Uri? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Use the uri variable declared outside this block
            Log.i("cameraLauncher", "Location de l'image: $uri")

            // Set the image URI to the Revue object
            //revue?.image = uri.toString()
            // Update the ImageView in the layout with the captured image
            binding.photosoulier?.setImageURI(uri)
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
        if (revue != null) {
            binding.editTextTitre.setText(revue!!.titre)
            binding.editTextREvue.setText(revue!!.commentaire)
            revue?.image?.let { binding.photosoulier?.setImageResource(it) }
        }

        binding.buttonphoto.setOnClickListener {
            // Check if permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Request camera permission if not granted
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                // Permission already granted, launch camera
                launchCamera()
            }
        }

        binding.button3.setOnClickListener {
            if (binding.editTextREvue.text.isBlank() || binding.editTextTitre.text.isBlank()) {
                Toast.makeText(applicationContext, "SVP remplir les champs obligatoire", Toast.LENGTH_SHORT).show()
            } else {
                // Your code for saving the revue
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
            //cameraLauncher.launch(cameraIntent)
        }
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_$timestamp", ".jpg", storageDir)
    }

}
