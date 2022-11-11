package com.jetbrains.mymediaplayernew

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jetbrains.mymediaplayernew.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private var mService : Messenger? = null
    private lateinit var mBoundServiceIntent : Intent
    private var mServiceBound = false
    private lateinit var binding: ActivityMainBinding

    private val mServiceConnection = object  : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = Messenger(service)
            mServiceBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mService = null
            mServiceBound = true
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnPlay.setOnClickListener {
                if (mServiceBound) {
                    try {
                        mService?.send(Message.obtain(null, MediaService.PLAY,0,0))
                    } catch (e : RemoteException) {
                        e.printStackTrace()
                    }

                }
            }
            btnStop.setOnClickListener {
                if (mServiceBound) {
                    try {
                        mService?.send(Message.obtain(null, MediaService.STOP, 0, 0))
                    } catch (e : RemoteException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        mBoundServiceIntent = Intent(this, MediaService::class.java)
        mBoundServiceIntent.action = MediaService.ACTION_CREATE

        startService(mBoundServiceIntent)
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        unbindService(mServiceConnection)
        mBoundServiceIntent.action = MediaService.ACTION_DESTROY

        startService(mBoundServiceIntent)
    }
}