package pl.training.goodweather.forecast.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import pl.training.goodweather.databinding.ActivityForecastBinding
import pl.training.goodweather.forecast.common.hideKeyboard
import pl.training.goodweather.forecast.common.setDrawable
import pl.training.goodweather.forecast.viewmodel.DayForecastViewModel
import pl.training.goodweather.forecast.viewmodel.ForecastViewModel

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
        getCityName()?.let {  city ->
            binding.cityNameEditText.setText(city)
            viewModel.refreshForecast(city)
        }
    }

    private fun initView() {
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        binding.forecastRecyclerView.adapter = forecastAdapter
    }

    private fun bindView() {
        viewModel.currentForecast.observe(this, ::updateView)
        binding.okButton.setOnClickListener {
            val cityName = binding.cityNameEditText.text.toString()
            if (cityName.isNotBlank()) {
                it.hideKeyboard()
                setCityName(cityName)
                viewModel.refreshForecast(cityName)
            }
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

    private fun setCityName(cityName: String)  = getSharedPreferences(packageName, MODE_PRIVATE)
        .edit()
        .putString(cityKey, cityName)
        .apply()

    private fun getCityName() = getSharedPreferences(packageName, MODE_PRIVATE)
        .getString(cityKey, defaultCity)

}