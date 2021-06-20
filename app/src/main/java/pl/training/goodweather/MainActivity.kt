package pl.training.goodweather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.training.goodweather.GoodWeatherApplication.Companion.applicationGraph
import pl.training.goodweather.common.UserPreferences
import pl.training.goodweather.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore

    @Inject
    lateinit var userPreferences: UserPreferences

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            applicationGraph.inject(this)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setSupportActionBar(binding.toolbar)
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            appBarConfiguration = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfiguration)
            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            navView.setupWithNavController(navController)

            PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(userPreferences)

            firebaseExample()
        }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.SettingsFragment -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.SettingsFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun firebaseExample() {
        val user = mapOf(
            "firstName" to "Jan",
            "lastName" to "Kowalski",
            "email" to "jan.kowalski@trainig.pl"
        )

        val users = db.collection("users")

        /*users.add(user)
            .addOnSuccessListener {
                Log.d("###", "Document: ${it.id}")
            }
            .addOnFailureListener {
                Log.d("###", "Error: ${it}")
            }*/

        users.whereEqualTo("firstName", "Jan")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    Log.d("###", "Document: ${document.data}")
                }
            }
            .addOnFailureListener {
                Log.d("###", "Error: ${it}")
            }

        users.document("Fe7eQrR0rrQC0tnN020B").addSnapshotListener { snapshot, error ->
            if (snapshot != null) {
                Log.d("###", "Update: ${snapshot.data}")
            }

        }

        users.document("Fe7eQrR0rrQC0tnN020B")
            .delete()
            .addOnSuccessListener {
                Log.d("###", "Document deleted")
            }
            .addOnFailureListener {
                Log.d("###", "Error: ${it}")
            }

    }

}