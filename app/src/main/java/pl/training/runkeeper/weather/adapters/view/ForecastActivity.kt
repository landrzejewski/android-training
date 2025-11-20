package pl.training.runkeeper.weather.adapters.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import pl.training.runkeeper.common.enableSafeArea
import pl.training.runkeeper.common.hideKeyboard
import pl.training.runkeeper.common.linearManagerWithScreenOrientation
import pl.training.runkeeper.common.loadDrawable
import pl.training.runkeeper.databinding.ActivityForecastBinding

class ForecastActivity : AppCompatActivity() {

    private val viewModel: ForecastViewModel by viewModels()
    private val recyclerViewAdapter = ForecastRecyclerViewAdapter()
    private lateinit var binding: ActivityForecastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        enableSafeArea(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.nextDaysForecastRecycler.adapter = recyclerViewAdapter
        binding.nextDaysForecastRecycler.layoutManager = linearManagerWithScreenOrientation(this)
        viewModel.forecast.observe(this, ::onUpdate)
        binding.checkButton.setOnClickListener(::onForecastCheck)
    }

    private fun onUpdate(forecast: List<DayForecastViewModel>) {
        if (forecast.isNotEmpty()) {
            val currentForecast = forecast.first()
            with(binding) {
                iconImage.loadDrawable(currentForecast.iconName)
                descriptionText.text = currentForecast.description
                temperatureText.text = currentForecast.temperature
                pressureText.text = currentForecast.pressure
                recyclerViewAdapter.update(forecast.drop(1))
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