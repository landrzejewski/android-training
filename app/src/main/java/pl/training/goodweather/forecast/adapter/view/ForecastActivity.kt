package pl.training.goodweather.forecast.adapter.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import pl.training.goodweather.common.getProperty
import pl.training.goodweather.common.hideKeyboard
import pl.training.goodweather.common.setDrawable
import pl.training.goodweather.common.setProperty
import pl.training.goodweather.databinding.ActivityForecastBinding

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding

    private val cityKey = "cityName"
    private val defaultCity = "Warsaw"
    private val viewModel: ForecastViewModel by viewModels()
    private val forecastAdapter = ForecastAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        bindView()
        getProperty(cityKey, defaultCity)?.let {
            refreshForecast(it)
            binding.cityNameEditText.setText(it);
        }
    }

    private fun initView() {
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        binding.forecastRecyclerView.adapter = forecastAdapter
    }

    private fun bindView() {
        viewModel.currentForecast.observe(this, ::updateView)
        binding.okButton.setOnClickListener {
            it.hideKeyboard()
            refreshForecast(binding.cityNameEditText.text.toString())
        }
    }

    private fun refreshForecast(city: String) {
        if (city.isNotBlank()) {
            setProperty(cityKey, city)
            viewModel.refreshForecast(city)
        }
    }

    private fun  updateView(forecast: List<DayForecastViewModel>) {
        with(forecast.first()) {
           binding.iconImageView.setDrawable(icon)
           binding.descriptionTextView.text = description
           binding.temperatureTextView.text = temperature
           binding.pressureTextView.text = pressure
        }
        forecastAdapter.update(forecast.drop(1))
    }

}