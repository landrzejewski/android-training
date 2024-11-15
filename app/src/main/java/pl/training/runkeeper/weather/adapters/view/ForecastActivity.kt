package pl.training.runkeeper.weather.adapters.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pl.training.runkeeper.common.enableSafeArea
import pl.training.runkeeper.common.hideKeyboard
import pl.training.runkeeper.common.setDrawable
import pl.training.runkeeper.databinding.ActivityForecastBinding

class ForecastActivity : AppCompatActivity() {

    private val viewModel: ForecastViewModel by viewModels()
    private lateinit var binding: ActivityForecastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        enableSafeArea(binding.main)
        initView()
    }

    private fun initView() {
       viewModel.forecast.observe(this, ::update)
       binding.checkButton.setOnClickListener(::onForecastCheck)
    }

    private fun update(forecast: List<DayForecastViewModel>) {
        if (forecast.isNotEmpty()) {
            val currentForecast = forecast.first()
            with(binding) {
                iconImage.setDrawable(currentForecast.iconName)
                descriptionText.text = currentForecast.description
                temperatureText.text = currentForecast.temperature
                pressureText.text = currentForecast.pressure
            }
        }
    }

    private fun onForecastCheck(view: View) {
        val city = binding.cityNameEdit.text.toString()
        if (city.isNotEmpty()) {
            view.hideKeyboard()
            viewModel.refreshForecast(city)
        }
    }

}