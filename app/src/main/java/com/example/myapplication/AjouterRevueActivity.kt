package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityActiviterAjouterSoulierBinding
import com.example.myapplication.databinding.ActivityAjouterRevueBinding
import com.google.gson.Gson

class AjouterRevueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAjouterRevueBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAjouterRevueBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button3.setOnClickListener {

            if (binding.editTextREvue.text.isBlank() && binding.editTextTitre.text.isBlank()){
                Toast.makeText(applicationContext, "SVP remplir les champs obligatoire", Toast.LENGTH_SHORT).show()

            }
            else{
                val revue =Revue(binding.editTextREvue.text.toString(),
                    binding.editTextTitre.text.toString(),"me",4f,null)

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