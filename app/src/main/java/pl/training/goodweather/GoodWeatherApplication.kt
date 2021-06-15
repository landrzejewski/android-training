package pl.training.goodweather

import android.app.Application
import pl.training.goodweather.configuration.ApplicationGraph
import pl.training.goodweather.configuration.DaggerApplicationGraph

class GoodWeatherApplication : Application() {

    companion object {

        lateinit var applicationGraph: ApplicationGraph
            private set

    }

    override fun onCreate() {
        super.onCreate()
        applicationGraph = DaggerApplicationGraph.builder()
            .build()
    }

}