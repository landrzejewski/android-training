package pl.training.runkeeper.weather.adapters.view

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import pl.training.runkeeper.R
import pl.training.runkeeper.common.ViewState
import pl.training.runkeeper.common.ViewState.Failure
import pl.training.runkeeper.common.ViewState.Initial
import pl.training.runkeeper.common.ViewState.Processing
import pl.training.runkeeper.common.ViewState.Success
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
        viewModel.viewState.observe(this, ::onUpdate)
        binding.checkButton.setOnClickListener(::onForecastCheck)
    }

    private fun onUpdate(viewState: ViewState) {
        binding.progressIndicator.visibility = GONE
        when(viewState) {
            is Initial -> initialView()
            is Processing -> processingView()
            is Success<*> -> forecastView(viewState.get())
            is Failure -> errorView(viewState.messageId)
        }
    }

    private fun initialView() {
        val icon = AppCompatResources.getDrawable(this, R.drawable.ic_empty)
        binding.iconImage.setImageDrawable(icon)
    }

    private fun processingView() {
        val icon = AppCompatResources.getDrawable(this, R.drawable.ic_empty)
        binding.iconImage.setImageDrawable(icon)
        binding.descriptionText.text = ""
        binding.temperatureText.text = ""
        binding.pressureText.text = ""
        recyclerViewAdapter.update(emptyList())
        binding.progressIndicator.visibility = VISIBLE
    }

    private fun forecastView(forecast: List<DayForecastViewModel>) {
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

    private fun errorView(messageId: Int) {
        Toast.makeText(this, getString(messageId), LENGTH_LONG).show()
    }

    private fun onForecastCheck(view: View) {
        val city = binding.cityNameEdit.text.toString()
        if (city.isNotEmpty()) {
            view.hideKeyboard()
            viewModel.refreshForecast(city)
        }
    }

}