package com.my.televip

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import java.io.IOException

object Audio {

    @JvmField
    var playing: Boolean = false

    private var mediaPlayer: MediaPlayer? = null

    @JvmStatic
    fun init() {
        mediaPlayer = MediaPlayer().apply {
            @Suppress("DEPRECATION")
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            setAudioAttributes(audioAttributes)
        }
    }

    @JvmStatic
    fun start() {
        val player = mediaPlayer ?: run {
            init()
            mediaPlayer!!
        }
        val num = (Math.random() * 3).toInt()
        val audioUrl = when (num) {
            0 -> "https://qurango.net/radio/abdulbasit_abdulsamad_mojawwad"
            1 -> "https://qurango.net/radio/yasser_aldosari"
            else -> "https://backup.qurango.net/radio/maher"
        }

        try {
            player.reset()
            player.setDataSource(audioUrl)
            player.prepareAsync()
            player.setOnPreparedListener { mp ->
                mp.start()
                playing = true
            }
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException, is IllegalStateException, is IOException -> {
                    playing = false
                    init()
                }
                else -> throw e
            }
        }
    }

    @JvmStatic
    fun stop() {
        val player = mediaPlayer
        if (player != null && player.isPlaying) {
            player.stop()
        }
        player?.release()
        init()
        playing = false
    }
}
