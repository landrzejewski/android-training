package pl.training.goodweather.forecast.adapter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import pl.training.goodweather.GoodWeatherApplication
import pl.training.goodweather.GoodWeatherApplication.Companion.applicationGraph
import pl.training.goodweather.R
import pl.training.goodweather.common.getProperty
import pl.training.goodweather.common.hideKeyboard
import pl.training.goodweather.common.logging.Logger
import pl.training.goodweather.common.setDrawable
import pl.training.goodweather.common.setProperty
import pl.training.goodweather.databinding.FragmentForecastBinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ForecastFragment : Fragment() {

    private lateinit var binding: FragmentForecastBinding
    @Inject
    lateinit var logger: Logger

    private val cityKey = "cityName"
    private val defaultCity = "Warsaw"
    private val viewModel: ForecastViewModel by activityViewModels()
    private val forecastAdapter = ForecastAdapter()
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        applicationGraph.inject(this)
        binding = FragmentForecastBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindView()
        getProperty(cityKey, defaultCity)?.let {
            refreshForecast(it)
            binding.cityNameEditText.setText(it)
        }
    }

    private fun initView() {
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        binding.forecastRecyclerView.adapter = forecastAdapter
    }

    private fun bindView() {
        viewModel.currentForecast.observe(viewLifecycleOwner, ::updateView)
        binding.cityNameEditText.textChanges()
            .map { it.toString() }
            .debounce(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::refreshForecast)  { logger.log(it.toString()) }
            .addTo(disposables)
        binding.okButton.setOnClickListener {
            it.hideKeyboard()
            refreshForecast(binding.cityNameEditText.text.toString())
        }
        binding.detailsButton.setOnClickListener {
            findNavController().navigate(R.id.showForecastDetails)
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

    override fun onDetach() {
        super.onDetach()
        disposables.clear()
    }

}