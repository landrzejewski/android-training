package pl.training.runkeeper.weather.adapters.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.training.runkeeper.R
import pl.training.runkeeper.common.loadDrawable
import pl.training.runkeeper.databinding.ItemDayForecastBinding
import pl.training.runkeeper.weather.adapters.view.ForecastRecyclerViewAdapter.ViewHolder

class ForecastRecyclerViewAdapter(private var forecast: List<DayForecastViewModel> = emptyList()) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_day_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.update(forecast[position])

    override fun getItemCount() = forecast.size

    fun update(forecast: List<DayForecastViewModel>) {
        this.forecast = forecast
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = ItemDayForecastBinding.bind(view)

        fun update(viewModel: DayForecastViewModel) {
            binding.itemIconImage.loadDrawable(viewModel.iconName)
            binding.itemTemperatureText.text = viewModel.temperature
            binding.itemDateText.text = viewModel.date
        }

    }

}