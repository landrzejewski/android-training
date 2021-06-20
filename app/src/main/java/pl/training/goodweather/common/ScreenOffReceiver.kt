package pl.training.goodweather.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ScreenOffReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_SCREEN_OFF == action) {
            Log.d("###", "Screen is off")
        } else if (Intent.ACTION_SCREEN_ON == action) {
            Log.d("###", "Screen is on")
        }
    }

}