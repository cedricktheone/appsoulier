package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var listeRevues: MutableList<Revue> =mutableListOf(
        Revue("LOL","DDDD","Ced",3,R.drawable.soulier),
        Revue("LOL","DDDD","Ced",2,R.drawable.soulier),
        Revue("LOL","DDDD","Ced",5,R.drawable.soulier),

    )

var listeSoulier: MutableList<Soulier> = mutableListOf(
        Soulier(listeRevues,"DDDD",20,2,R.drawable.soulier),
    Soulier(listeRevues,"DDDD",40,4,R.drawable.soulier),
    Soulier(listeRevues,"DDDD",12,3,R.drawable.soulier),

    )

    val adapter = AdapteurSoulier(this,listeSoulier)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.listViewsoulier.adapter = adapter
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ajouter ->{
                startActivity(Intent(this,ActiviterAjouterSoulier::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }
}