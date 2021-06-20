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
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("###", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("###", "Error adding document", e)
            }

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("###", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("###", "Error getting documents.", exception)
            }

        db.collection("users").whereEqualTo("first", "Ada")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("###", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("###", "Error getting documents: ", exception)
            }

        db.collection("users").document("1wBtF9ijsciZgsRCfvoa").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("###", "Listen failed.", e)
                return@addSnapshotListener
            }

            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites())
                "Local"
            else
                "Server"

            if (snapshot != null && snapshot.exists()) {
                Log.d("###", "$source data: ${snapshot.data}")
            } else {
                Log.d("###", "$source data: null")
            }
        }

        db.collection("users").document("1wBtF9ijsciZgsRCfvoa")
            .delete()
            .addOnSuccessListener { Log.d("###", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("###", "Error deleting document", e) }
    }

}