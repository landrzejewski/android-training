package pl.training.goodweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val tag = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "On create")
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.sayHelloButton).setOnClickListener(::sayHello)
    }

    private fun sayHello(button: View) {
        val message = findViewById<TextView>(R.id.message)
        message.text = getString(R.string.hello_android)
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "On start")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "On resume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "On pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "On stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "On destroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "On restart")
    }

}