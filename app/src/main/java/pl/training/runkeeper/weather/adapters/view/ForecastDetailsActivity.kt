package pl.training.runkeeper.weather.adapters.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import pl.training.runkeeper.common.enableSafeArea
import pl.training.runkeeper.databinding.ActivityForecastDetailsBinding

class ForecastDetailsActivity : AppCompatActivity() {

    private val tag = ForecastDetailsActivity::class.java.canonicalName
    private lateinit var binding: ActivityForecastDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        enableSafeArea(binding.main)
        val description = intent.getStringExtra("description")
        Log.i(tag, "### $description")
    }

}