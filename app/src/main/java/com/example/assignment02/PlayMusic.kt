package com.example.assignment02

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.net.Uri
import android.util.Log

class PlayMusic : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val channel1 = "channel1"

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channel1,
                "music",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pending_intent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // 알림 생성
        val notification: Notification = NotificationCompat.Builder(this, channel1)
            .setContentTitle("음악 재생 중...")
            .setContentText("백그라운드에서 음악이 재생되고 있습니다.")
            .setSmallIcon(androidx.core.R.drawable.ic_call_answer)
            .setContentIntent(pending_intent)
            .setAutoCancel(true)
            .build()

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val uri_string = intent.getStringExtra("uri")
        Log.d("PlayMusic", "URI: $uri_string")
        uri_string?.let {
            val uri = Uri.parse(it)
            musicPlay(uri)
        }
        return START_STICKY
    }

    private fun musicPlay(uri: Uri) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, uri)
            setOnPreparedListener {
                it.start()
            }
            prepareAsync()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}