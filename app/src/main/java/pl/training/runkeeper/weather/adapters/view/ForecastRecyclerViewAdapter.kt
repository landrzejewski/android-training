package pl.training.runkeeper.weather.adapters.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.training.runkeeper.R
import pl.training.runkeeper.common.setDrawable
import pl.training.runkeeper.databinding.ItemDayForecastBinding

class ForecastRecyclerViewAdapter(private var forecast: List<DayForecastViewModel> = emptyList()) :
    RecyclerView.Adapter<ForecastRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemDayForecastBinding.bind(view)

        fun update(viewModel: DayForecastViewModel) {
            binding.itemIconImage.setDrawable(viewModel.iconName)
            binding.itemTemperatureText.text = viewModel.temperature
            binding.itemDateText.text = viewModel.date
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_day_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = forecast.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.update(forecast[position])

    @SuppressLint("NotifyDataSetChanged")
    fun update(forecast: List<DayForecastViewModel>) {
        this.forecast = forecast
        notifyDataSetChanged()
    }

}