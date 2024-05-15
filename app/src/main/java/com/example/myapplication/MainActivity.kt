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
import com.google.gson.reflect.TypeToken
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listeSoulier: MutableList<Soulier> = mutableListOf()
    private lateinit var adapter: AdapteurSoulier

    private val soulierLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentData = result.data
            val jsonSoulier = intentData?.getStringExtra("soulier")


            val gson = Gson()
            val soulier: Soulier? = gson.fromJson(jsonSoulier, Soulier::class.java)

            soulier?.let {
                // Update adapter with the new Soulier object
                val existingItem = listeSoulier.find { it.nom == soulier.nom }
                if (existingItem != null) {
                    existingItem.revues = soulier.revues
                    existingItem.prix = soulier.prix
                } else {
                    listeSoulier.add(soulier)
                }

                adapter.notifyDataSetChanged()
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load data from file
        loadDataFromStorage()
        if (listeSoulier.isEmpty()){


            listeSoulier= mutableListOf(
                Soulier("DDDD",20,null),
                Soulier("DDDD",40,null),
                Soulier("DDDD",12,null)
            )

        }
        adapter = AdapteurSoulier(this, listeSoulier)
        binding.listViewsoulier.adapter = adapter

        binding.listViewsoulier.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this, ActiviterAjouterSoulier::class.java)
            val item = adapter.getItem(i) as? Soulier
            val gson = Gson()
            val soulierJson = gson.toJson(item)

            intent.putExtra("soulier", soulierJson)
            soulierLauncher.launch(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_SOULIER && resultCode == Activity.RESULT_OK) {
            val updatedSoulierJson = data?.getStringExtra("updatedSoulier")
            val gson = Gson()
            val updatedSoulier = gson.fromJson(updatedSoulierJson, Soulier::class.java)

            // Update the corresponding Soulier object in the list
            val existingSoulierIndex = listeSoulier.indexOfFirst { it.nom == updatedSoulier.nom }
            if (existingSoulierIndex != -1) {
                listeSoulier[existingSoulierIndex] = updatedSoulier
                adapter.notifyDataSetChanged()
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_ADD_SOULIER = 100
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ajouter -> {
                val intent = Intent(this, Formajoutsoulier::class.java)
                soulierLauncher.launch(intent)
            }
            R.id.pageaceuille->{
                val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("user")
                editor.apply()
                val intent = Intent(
                    applicationContext,
                    Login::class.java
                )
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("EXIT", true)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        // Save data to file
        saveDataToStorage()
    }

    private fun saveDataToStorage() {
        try {
            val gson = Gson()
            val jsonString = gson.toJson(listeSoulier)
            File("ListeData.json").writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadDataFromStorage() {
        try {
            val gson = Gson()
            val jsonString = File("ListeData.json").readText()
            val listType = object : TypeToken<MutableList<Soulier>>() {}.type
            val newlist: MutableList<Soulier> = gson.fromJson(jsonString, listType)
            listeSoulier.clear()
            listeSoulier.addAll(newlist)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
