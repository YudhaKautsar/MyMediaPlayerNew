package com.jetbrains.mymediaplayernew

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jetbrains.mymediaplayernew.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mMediaPlayer: MediaPlayer
    private var isReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnPlay.setOnClickListener {
                if (!isReady) {
                    mMediaPlayer.prepareAsync()
                } else {
                    if (mMediaPlayer.isPlaying) {
                        mMediaPlayer.pause()
                    } else {
                        mMediaPlayer.start()
                    }
                }
            }
            btnStop.setOnClickListener {
                if (mMediaPlayer.isPlaying || isReady) {
                    mMediaPlayer.stop()
                    isReady = false
                }
            }
        }

        init()
    }

    private fun init() {
        mMediaPlayer = MediaPlayer()
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mMediaPlayer.setAudioAttributes(attribute)
        val afd = applicationContext.resources.openRawResourceFd(R.raw.guitar_background)
        try {
            mMediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e : IOException) {
            e.printStackTrace()
        }

        mMediaPlayer.setOnPreparedListener{
            isReady = true
            mMediaPlayer.start()
        }

        mMediaPlayer.setOnErrorListener { _, _, _ -> false }
    }
}