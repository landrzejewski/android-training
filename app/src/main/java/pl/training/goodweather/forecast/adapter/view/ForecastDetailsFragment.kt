package pl.training.goodweather.forecast.adapter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.training.goodweather.databinding.FragmentForecastDetailsBinding

class ForecastDetailsFragment : Fragment() {

    private lateinit var binding: FragmentForecastDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentForecastDetailsBinding.inflate(inflater)
        return binding.root
    }

}