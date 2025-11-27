package pl.training.runkeeper.weather.adapters.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import pl.training.runkeeper.databinding.FragmentForecastDetailsBinding

class ForecastDetailsFragment : Fragment() {

    private val viewModel: ForecastViewModel by activityViewModels()
    private lateinit var binding: FragmentForecastDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentForecastDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("###", viewModel.selectedDayForecast.toString())
    }

}