package pl.training.runkeeper.common.components

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.app.Notification.CATEGORY_SERVICE
import android.app.Notification.VISIBILITY_PRIVATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_NONE
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.NotificationManagerCompat
import pl.training.runkeeper.R
import java.util.Timer
import java.util.TimerTask

class SchedulerService : Service() {

    private var counter = 100
    private val timer = Timer()
    private val timerTask: TimerTask = object : TimerTask() {

        override fun run() {
            counter--
            Log.i("###", "Time to execution ${counter}s")
            if (counter <= 0) {
                timer.cancel()
                Log.i("###", "Executing task")
            }
        }

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        init()
        Log.i("###", "SchedulerService started")
    }

    private fun init() {
        val thisContext = this
        with(NotificationManagerCompat.from(this)) {
            if (checkSelfPermission(thisContext, POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
                return
            }
            notify(ID, createNotification())
        }
    }

    private fun createNotification(): Notification {
        val channel = NotificationChannel(CLIENT_ID, CHANNEL_NAME, IMPORTANCE_NONE)
        channel.lightColor = Color.RED
        channel.lockscreenVisibility = VISIBILITY_PRIVATE
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        return Notification.Builder(this, CLIENT_ID)
            .setOngoing(true)
            .setContentTitle("Task is scheduled")
            .setSmallIcon(R.drawable.ic_sun)
            .setCategory(CATEGORY_SERVICE)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        timer.schedule(timerTask, 0, 1_000)
        return START_STICKY
    }

    companion object {

        private const val CLIENT_ID = "pl.training.runkeeper"
        private const val CHANNEL_NAME = "scheduling"
        private const val ID = 1

    }

}