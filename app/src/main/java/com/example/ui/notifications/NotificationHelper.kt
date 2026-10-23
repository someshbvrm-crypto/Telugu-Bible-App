package com.example.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.DailyVerse

object NotificationHelper {

    const val CHANNEL_ID = "telugu_bible_daily_channel"
    const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "డైలీ బైబిల్ వాక్యము & అధ్యయనం"
            val descriptionText = "ప్రతిరోజు ఉదయం మరియు సాయంత్రం బైబిల్ వాక్యాలు మరియు పఠన రిమైండర్లు"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showDailyVerseNotification(context: Context, dailyVerse: DailyVerse) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🕊️ ${dailyVerse.reference} • నేటి వాక్యము")
            .setContentText(dailyVerse.textTelugu)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${dailyVerse.textTelugu}\n\n\"${dailyVerse.devotionalThought}\"")
                    .setSummaryText("పరిశుద్ధ గ్రంథము • Telugu Bible")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        } catch (_: SecurityException) {
            // Handled gracefully if notification permission is not yet granted
        }
    }
}
