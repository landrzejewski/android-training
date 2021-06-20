package pl.training.goodweather.common

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log


class ScreenOffService : Service() {

    private lateinit var screenOffReceiver: ScreenOffReceiver

    override fun onBind(intent: Intent?): IBinder?  = null

    override fun onCreate() {
        super.onCreate()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)

        screenOffReceiver = ScreenOffReceiver()
        registerReceiver(screenOffReceiver, intentFilter)

        Log.d("##", "Registering receiver")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenOffReceiver)
        Log.d("##", "Receiver unregistered")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

}