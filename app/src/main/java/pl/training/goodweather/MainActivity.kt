package pl.training.goodweather

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pl.training.goodweather.databinding.ActivityMainBinding
import pl.training.goodweather.forecast.common.setDrawable
import pl.training.goodweather.forecast.viewmodel.DayForecastViewModel
import pl.training.goodweather.forecast.viewmodel.ForecastViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: ForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        bindView()
    }

    private fun initView() {

    }

    private fun bindView() {
        viewModel.currentForecast.observe(this, ::updateView)
        binding.okButton.setOnClickListener {
            val cityName = binding.cityNameEditText.text.toString()
            if (cityName.isNotBlank()) {
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
    }

}