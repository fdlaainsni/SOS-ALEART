package com.example.sosaleart

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "fire_alert_channel"
        private const val CHANNEL_NAME = "Peringatan Kebakaran"
        private const val CHANNEL_DESC = "Channel untuk notifikasi kebakaran"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Token baru: $token")
        // Kirim token ke server jika perlu
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "🔥 Peringatan Kebakaran!"
        val message = remoteMessage.notification?.body ?: "Kebakaran terdeteksi!"
        showNotification(this, title, message)
    }

    private fun showNotification(context: Context, title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                lightColor = Color.RED
                description = CHANNEL_DESC
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_fire) // Pastikan drawable ini ada
            .setContentTitle(title)
            .setContentText(message)
            .setColor(Color.RED)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Cek permission untuk Android 13+
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context)
                .notify(Random().nextInt(), builder.build())
        } else {
            Log.w("FCM", "Permission POST_NOTIFICATIONS belum diberikan")
        }
    }
}
