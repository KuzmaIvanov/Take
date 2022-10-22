package com.example.take

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {

        val i = Intent(p0, MainActivity::class.java)
        p1!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(p0, 0, i, 0)
        //PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(p0!!, "alarm_receiver_id")
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle("Alarm Manager")
            .setContentText("This is a content text")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(p0)
        notificationManager.notify(0, builder.build())
    }
}