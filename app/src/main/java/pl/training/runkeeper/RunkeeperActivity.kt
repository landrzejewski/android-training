package pl.training.runkeeper

import android.content.Intent.ACTION_AIRPLANE_MODE_CHANGED
import android.content.IntentFilter
import android.os.Bundle
import android.os.Environment.MEDIA_MOUNTED
import android.os.Environment.MEDIA_MOUNTED_READ_ONLY
import android.os.Environment.getExternalStorageState
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import pl.training.runkeeper.common.components.AirplaneModeReceiver
import pl.training.runkeeper.databinding.ActivityRunkeeperBinding

@AndroidEntryPoint
class RunkeeperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRunkeeperBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val airplaneModeReceiver = AirplaneModeReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunkeeperBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.ForecastFragment, R.id.TrackingFragment, R.id.ProfileFragment
        )) // root elements

        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.SettingsFragment -> {
            navController.navigate(R.id.SettingsFragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun componentExamples() {
        // Broadcast receiver

        val airplaneModeIntent = IntentFilter(ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, airplaneModeIntent)
        // unregisterReceiver(airplaneModeReceiver)

        // Content provider

        /*val values = ContentValues()
        values.put(DATE_COLUMN, Date().time)
        values.put(CONDITIONS_COLUMN, "Sunny")

        val uri = contentResolver.insert(CONTENT_URI, values)
        Log.i("###", uri.toString())*/

        /* val queryUri = Uri.parse("content://pl.training.runkeeper.common.components.ForecastProvider/forecast")
         contentResolver.query(queryUri, null, null, null)
             ?.let {
                 val dateColumnIdx = it.getColumnIndex(DATE_COLUMN)
                 val conditionsIdx = it.getColumnIndex(CONDITIONS_COLUMN)
                 while (it.moveToNext()) {
                     val date = Date(it.getLong(dateColumnIdx))
                     val conditions = it.getString(conditionsIdx)
                     Log.i("###", "$date:$conditions")
                 }
                 it.close()
             }*/

        // Service
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, SchedulerService::class.java))
        } else {
            startService(Intent(this, SchedulerService::class.java))
        }*/


        // App file storage

        /*openFileOutput("data.txt", MODE_PRIVATE).use {
            it.write("To jest text".toByteArray())
        }

        openFileInput("data.txt").bufferedReader()
            .forEachLine {
                Log.i("###", it)
            }

        // External file storage
        if (externalStorageExists() && externalStorageWritable()) {
            val path = File(getExternalFilesDir("Data"), "app_data.txt").toPath()
            Log.i("###", path.toString())
            Files.write(path, "Hello".toByteArray())
            Files.readAllLines(path).forEach {
                Log.i("###", it)
            }
        }*/

    }

    private fun externalStorageExists() = MEDIA_MOUNTED == getExternalStorageState()

    private fun externalStorageWritable() = MEDIA_MOUNTED_READ_ONLY != getExternalStorageState()

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airplaneModeReceiver)
        // stopService(Intent(this, SchedulerService::class.java))
    }


}