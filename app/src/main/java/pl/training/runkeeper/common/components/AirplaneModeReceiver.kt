package pl.training.runkeeper.common.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AirplaneModeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getBooleanExtra("state", false) ?: return
        Log.i("###", "Airplane mode: $state")
    }

}