package ru.startandroid.develop.servicebindinglocal

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    val LOG_TAG = "myLogs"
    var bound = false
    var sConn: ServiceConnection? = null
    var intent2: Intent? = null
    var myService: MyService? = null
    var tvInterval: TextView? = null
    var interval: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInterval = findViewById<View>(R.id.tvInterval) as TextView
        intent2 = Intent(this, MyService::class.java)
        sConn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected")
                myService = (binder as MyService.MyBinder).service
                bound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected")
                bound = false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(intent2, sConn!!, 0)
    }

    override fun onStop() {
        super.onStop()
        if (!bound) return
        unbindService(sConn!!)
        bound = false
    }

    fun onClickStart(v: View) {
        startService(intent2)
    }

    fun onClickUp(v: View) {
        if (!bound) return
        interval = myService!!.upInterval(500)
        tvInterval!!.text = "interval = $interval"
    }

    fun onClickDown(v: View) {
        if (!bound) return
        interval = myService!!.downInterval(500)
        tvInterval!!.text = "interval = $interval"
    }
}