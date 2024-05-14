import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ActiviterAjouterSoulier
import com.example.myapplication.AdapteurSoulier
import com.example.myapplication.Login
import com.example.myapplication.R
import com.example.myapplication.Soulier
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listeSoulier: MutableList<Soulier> = mutableListOf()

    private val soulierLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentData = result.data
            val jsonSoulier = intentData?.getStringExtra("soulier")

            // Parse JSON string into Soulier object
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

    private val adapter = AdapteurSoulier(this, listeSoulier)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load data from file
        loadDataFromStorage()

        binding.listViewsoulier.adapter = adapter

        binding.listViewsoulier.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this, ActiviterAjouterSoulier::class.java)
            val item = adapter.getItem(i) as? Soulier

            // Serialize Soulier object to JSON string
            val gson = Gson()
            val soulierJson = gson.toJson(item)

            intent.putExtra("soulier", soulierJson)
            soulierLauncher.launch(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ajouter -> {
                startActivity(Intent(this, Login::class.java))
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
            openFileOutput("ListeData.data", Context.MODE_PRIVATE).use { fileOutputStream ->
                ObjectOutputStream(fileOutputStream).use { objectOutputStream ->
                    objectOutputStream.writeObject(listeSoulier)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadDataFromStorage() {
        try {
            openFileInput("ListeData.data").use { fileInputStream ->
                ObjectInputStream(fileInputStream).use { objectInputStream ->
                    @Suppress("UNCHECKED_CAST")
                    val newlist = objectInputStream.readObject() as? MutableList<Soulier>
                    newlist?.let {
                        listeSoulier = it
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
}
