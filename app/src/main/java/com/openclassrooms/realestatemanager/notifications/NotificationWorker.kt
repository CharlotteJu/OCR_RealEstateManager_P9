package com.openclassrooms.realestatemanager.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.NOTIFICATION_CHANNEL_ID
import com.openclassrooms.realestatemanager.utils.NOTIFICATION_CHANNEL_NAME
import com.openclassrooms.realestatemanager.utils.NOTIFICATION_ID
import com.openclassrooms.realestatemanager.views.activities.MainActivity

object NotificationWorker
{
    fun showNotification(context: Context)
    {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.notification_housing_add))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        manager.notify(NOTIFICATION_ID, builder.build())
    }
}