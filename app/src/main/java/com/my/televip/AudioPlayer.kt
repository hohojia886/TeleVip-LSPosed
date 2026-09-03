package com.my.televip

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer

object AudioPlayer {

    @JvmStatic
    @Volatile
    var isPlaying: Boolean = false
        private set

    private var mediaPlayer: MediaPlayer? = null

    @JvmStatic
    fun init() {
        if (mediaPlayer == null) {
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
    }

    @JvmStatic
    fun start() {
        val urls = listOf(
            "https://qurango.net/radio/abdulbasit_abdulsamad_mojawwad",
            "https://qurango.net/radio/yasser_aldosari",
            "https://backup.qurango.net/radio/maher"
        )
        val audioUrl = urls.random()

        try {
            init()
            mediaPlayer?.let { player ->
                player.reset()
                player.setDataSource(audioUrl)
                player.prepareAsync()
                player.setOnPreparedListener {
                    player.start()
                    isPlaying = true
                }
                player.setOnErrorListener { _, _, _ ->
                    isPlaying = false
                    true
                }
            }
        } catch (_: Throwable) {
            isPlaying = false
            init()
        }
    }

    @JvmStatic
    fun stop() {
        try {
            mediaPlayer?.takeIf { it.isPlaying }?.stop()
            mediaPlayer?.release()
        } catch (_: Throwable) {
        } finally {
            mediaPlayer = null
            isPlaying = false
        }
    }
}
