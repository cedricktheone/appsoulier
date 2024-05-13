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
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AjouterRevueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAjouterRevueBinding
    private var revue: Revue? = null
    private var uri: Uri? = null

    private val pickerlauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        binding.photosoulier?.setImageURI(uri)
    }

    private val requesPermissionlauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
        if(!result){
            Toast.makeText(this,"La permission a été refusé",Toast.LENGTH_SHORT).show()
        }
    }
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
        sucess->

        if (sucess){
            Log.i("cameraLauncher","Location de l'image: $uri")
        }
        else{
            Log.i("cameraLauncher","could not save: $uri")
        }
    }


    private fun CreateImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG+${timestamp}",".jpg",storageDir)

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
        var accorded = false
        binding.buttonphoto.setOnClickListener {
            val fichier = CreateImageFile()

            try {
                uri = FileProvider.getUriForFile(this,"com.example.myapplication.fileprovider",fichier)
            }
            catch (e:Exception){
                e.printStackTrace()
            }
            cameraLauncher.launch(uri)
            //pickerlauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.button3.setOnClickListener {

            if (binding.editTextREvue.text.isBlank() || binding.editTextTitre.text.isBlank()) {
                Toast.makeText(applicationContext, "SVP remplir les champs obligatoire", Toast.LENGTH_SHORT).show()

            } else {
                if (revue != null) {
                    // Modify attributes of existing revue
                    revue!!.titre = binding.editTextTitre.text.toString()
                    revue!!.commentaire = binding.editTextREvue.text.toString()
                } else {
                    // Create a new revue
                    revue = Revue(
                        binding.editTextTitre.text.toString(),
                        binding.editTextREvue.text.toString(),
                        "me",
                        4f,
                        null
                    )
                }

                val gson = Gson()
                val revueJson = gson.toJson(revue)

                val intent = Intent()
                intent.putExtra("revueJson", revueJson)

                setResult(Activity.RESULT_OK, intent)
                finish()

            }
        }

    }
}
