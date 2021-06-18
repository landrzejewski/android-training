package pl.training.goodweather.configuration

import dagger.Component
import pl.training.goodweather.MainActivity
import pl.training.goodweather.forecast.ForecastModule
import pl.training.goodweather.forecast.adapter.view.ForecastFragment
import pl.training.goodweather.forecast.adapter.view.ForecastViewModel
import pl.training.goodweather.tracking.adapter.view.TrackingFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ForecastModule::class])
interface ApplicationGraph {

    fun inject(forecastViewModel: ForecastViewModel)

    fun inject(forecastFragment: ForecastFragment)

    fun inject(trackingFragment: TrackingFragment)

    fun inject(mainActivity: MainActivity)

}