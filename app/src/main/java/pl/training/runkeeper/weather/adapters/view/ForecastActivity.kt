package pl.training.runkeeper.weather.adapters.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import pl.training.runkeeper.R
import pl.training.runkeeper.common.ViewState
import pl.training.runkeeper.common.ViewState.Failure
import pl.training.runkeeper.common.ViewState.Initial
import pl.training.runkeeper.common.ViewState.Processing
import pl.training.runkeeper.common.ViewState.Success
import pl.training.runkeeper.common.enableSafeArea
import pl.training.runkeeper.common.hideKeyboard
import pl.training.runkeeper.common.linearManagerWithScreenOrientation
import pl.training.runkeeper.common.setDrawable
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
        enableSafeArea(binding.main)
        initView()
    }

    private fun initView() {
        viewModel.viewState.observe(this, ::update)
        binding.nextDaysForecastRecycler.layoutManager = linearManagerWithScreenOrientation(this)
        binding.nextDaysForecastRecycler.adapter = recyclerViewAdapter
        binding.checkButton.setOnClickListener(::onForecastCheck)
    }

    private fun update(viewState: ViewState) = when (viewState) {
        is Initial -> initialView()
        is Processing -> processingView()
        is Success<*> -> forecastView(viewState.get())
        is Failure -> errorView(viewState.message)
    }

    private fun initialView() {
        val icon = getDrawable(this, R.drawable.ic_empty)
        binding.iconImage.setImageDrawable(icon)
    }

    private fun processingView() {
        binding.descriptionText.text = "Loading..."
    }

    private fun forecastView(forecast: List<DayForecastViewModel>) {
        val currentForecast = forecast.first()
        binding.iconImage.setDrawable(currentForecast.iconName)
        binding.descriptionText.text = currentForecast.description
        binding.temperatureText.text = currentForecast.temperature
        binding.pressureText.text = currentForecast.pressure
        recyclerViewAdapter.update(forecast.drop(1))
    }

    private fun errorView(message: String) {
        val icon = getDrawable(this, R.drawable.ic_empty)
        binding.iconImage.setImageDrawable(icon)
        binding.descriptionText.text = ""
        binding.temperatureText.text = ""
        binding.pressureText.text = ""
        recyclerViewAdapter.update(emptyList())
        Toast.makeText(this, message, LENGTH_LONG).show()
    }

    private fun onForecastCheck(view: View) {
        val city = binding.cityNameEdit.text.toString()
        if (city.isNotEmpty()) {
            view.hideKeyboard()
            viewModel.refreshForecast(city)
        }
    }

}