package com.anxer.part3

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.anxer.part2.ITwoAidlInterface

class MyPart3Service : Service() {
    private var aidlInterface: ITwoAidlInterface? = null
    private lateinit var periodicRunnable: Runnable
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        periodicRunnable = object : Runnable {
            override fun run() {
                part2Conn()
                handler.postDelayed(this, 1000)
            }
        }
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("part3Service", "part3Service started from part3.")
        handler.post(periodicRunnable)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        handler.removeCallbacks(periodicRunnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private val part2Connect = object : ServiceConnection {
        @SuppressLint("LongLogTag")
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("part3ServiceConnectionStatus", "Service is connected.")
            aidlInterface = ITwoAidlInterface.Stub.asInterface(service)
            part2Conn()

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("part3ServiceConnectionStatus", "Service is disconnected.")
            unbindService(this)
        }

    }

    private fun part2Conn() {
        if (aidlInterface == null) {
            val intent = Intent("MyPart2Service")
            intent.setPackage("com.anxer.part2")
            bindService(intent, part2Connect, BIND_AUTO_CREATE)
            return
        }
        val nameToReverse = aidlInterface?.sendNameTo3()
        if (nameToReverse != "Default Part1") {
            val reverseOfIt = nameToReverse?.reversed()
            Log.d("part3Service", "nameToReverse: $nameToReverse")
            Log.d("part3Service", "Reversed Name: $reverseOfIt")
            if (nameToReverse.equals(
                    reverseOfIt,
                    true
                )
            ) {
                aidlInterface?.callBack(1)
                Log.d("part3Service", "Palindrome........................!")
            } else aidlInterface?.callBack(0)
        } else Log.d("part3Service", "Haven't got input yet...!")
    }
}