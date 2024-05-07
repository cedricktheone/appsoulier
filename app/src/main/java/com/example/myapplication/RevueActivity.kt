package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.RevuesBinding

class RevueActivity : AppCompatActivity() {
    private lateinit var binding: RevuesBinding


    private var listeRevues: MutableList<Revue> = mutableListOf(
        Revue("40000F","Épargne","Cedrick",4.5f,R.drawable.soulierliste),

        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RevuesBinding.inflate(layoutInflater)
        binding.

        setContentView(binding.root)
    }
}