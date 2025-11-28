package pl.training.runkeeper.weather.adapters.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnKeyListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import pl.training.runkeeper.R
import pl.training.runkeeper.common.view.ViewState
import pl.training.runkeeper.common.view.ViewState.Failure
import pl.training.runkeeper.common.view.ViewState.Initial
import pl.training.runkeeper.common.view.ViewState.Processing
import pl.training.runkeeper.common.view.ViewState.Success
import pl.training.runkeeper.common.view.hideKeyboard
import pl.training.runkeeper.common.view.linearManagerWithScreenOrientation
import pl.training.runkeeper.common.view.loadDrawable
import pl.training.runkeeper.databinding.FragmentForecastBinding
import pl.training.runkeeper.weather.adapters.view.ForecastViewModel.ViewData

class ForecastFragment : Fragment() {

    private val viewModel: ForecastViewModel by activityViewModels()
    private val recyclerViewAdapter = ForecastRecyclerViewAdapter()
    private lateinit var binding: FragmentForecastBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentForecastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.nextDaysForecastRecycler.adapter = recyclerViewAdapter
        binding.nextDaysForecastRecycler.layoutManager = linearManagerWithScreenOrientation(requireContext())
        viewModel.viewState.observe(this, ::onUpdate)
        binding.checkButton.setOnClickListener(::onForecastCheck)
        binding.cityNameEdit.setOnKeyListener(keyListener)
        viewModel.refreshForecastFromCache()
        recyclerViewAdapter.selectListener = ::onDayForecastSelect
    }

    private fun onDayForecastSelect(position: Int, selectedModel: DayForecastViewModel) {
        viewModel.selectedDayForecast = selectedModel
        findNavController().navigate(R.id.show_forecast_details)
    }

    val keyListener = OnKeyListener { view, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
            onForecastCheck(view)
            true
        } else {
            false
        }
    }

    private fun onUpdate(viewState: ViewState) {
        binding.progressIndicator.visibility = GONE
        when (viewState) {
            is Initial -> initialView()
            is Processing -> processingView()
            is Success<*> -> forecastView(viewState.get())
            is Failure -> errorView(viewState.messageId)
        }
    }

    private fun initialView() {
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_empty)
        binding.iconImage.setImageDrawable(icon)
    }

    private fun processingView() {
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_empty)
        binding.iconImage.setImageDrawable(icon)
        binding.descriptionText.text = ""
        binding.temperatureText.text = ""
        binding.pressureText.text = ""
        recyclerViewAdapter.update(emptyList())
        binding.progressIndicator.visibility = VISIBLE
    }

    private fun forecastView(viewData: ViewData) {
        val currentForecast = viewData.forecast.first()
        with(binding) {
            cityNameText?.text = viewData.city
            iconImage.loadDrawable(currentForecast.iconName)
            descriptionText.text = currentForecast.description
            temperatureText.text = currentForecast.temperature
            pressureText.text = currentForecast.pressure
            recyclerViewAdapter.update(viewData.forecast.drop(1))
        }
    }

    private fun errorView(messageId: Int) {
        Toast.makeText(requireContext(), getString(messageId), LENGTH_LONG).show()
    }

    private fun onForecastCheck(view: View) {
        val city = binding.cityNameEdit.text.toString()
        if (city.isNotEmpty()) {
            view.hideKeyboard()
            viewModel.refreshForecast(city)
        }
    }

}