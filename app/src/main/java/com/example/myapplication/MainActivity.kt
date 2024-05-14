package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var listeRevues: MutableList<Revue> =mutableListOf(
        Revue("LOL","DDDD","Ced",3f,R.drawable.soulier),
        Revue("haha","DDDD","Ced",2f,R.drawable.soulier),
        Revue("sfdfd","DDDD","Ced",5f,R.drawable.soulier),

    )

var listeSoulier: MutableList<Soulier> = mutableListOf(
        Soulier(listeRevues,"DDDD",5f,20,R.drawable.soulier),
    Soulier(listeRevues,"DDDD",2f,40,R.drawable.soulier),
    Soulier(listeRevues,"DDDD",0f,12,R.drawable.soulier),

    )
    val soulierLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentData = result.data
            val jsonSoulier = intentData?.getStringExtra("soulier")

            // Parse JSON string into Soulier object
            val gson = Gson()
            val soulier: Soulier? = gson.fromJson(jsonSoulier, Soulier::class.java)

            soulier?.let {
                // Update adapter with the new Soulier object
                // Assuming adapter and classList are accessible here
                // Update properties of existing Soulier if found, otherwise add new Soulier
                val existingItem = listeSoulier.find { it.nom == soulier.nom }
                if (existingItem != null) {
                    // Item exists, update its properties
                    existingItem.revues = soulier.revues
                    existingItem.prix = soulier.prix
                } else {
                    // Add new Soulier to the list
                    listeSoulier.add(soulier)
                }
                // Notify adapter about the data change
                adapter.notifyDataSetChanged()
            }
        }
    }

    val adapter = AdapteurSoulier(this,listeSoulier)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            openFileInput("ListeComptes.data").use { fileInputSream->
                ObjectInputStream(fileInputSream).use{ objectInputStream->
                    val newlist: MutableList<Soulier> = (objectInputStream.readObject() as MutableList<Soulier>)
                    if(newlist.size != 0){
                        listeSoulier = newlist
                    }
                }
            }
        }
        catch (e:IOException){
            e.printStackTrace()
        }






        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.listViewsoulier.adapter = adapter
        setContentView(binding.root)




        binding.listViewsoulier.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, ActiviterAjouterSoulier::class.java)
            val item = adapterView.getItemAtPosition(i) as Soulier

            // Serialize Soulier object to JSON string
            val gson = Gson()
            val soulierJson = gson.toJson(item)

            intent.putExtra("soulier", soulierJson)
            soulierLauncher.launch(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ajouter ->{
                startActivity(Intent(this,Login::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        try {
            openFileOutput("ListeData.data", Context.MODE_APPEND).use { fileOutputStream ->
                ObjectOutputStream(fileOutputStream).use { objectOutputStream ->
                    objectOutputStream.writeObject(listeSoulier)

                }
            }
        }
        catch (e: IOException){
            e.printStackTrace()
        }

    }
}