package com.iti.linguaquest.features.lockscreen.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.iti.linguaquest.MainActivity
import com.iti.linguaquest.R
import com.iti.linguaquest.features.lockscreen.domain.model.LockScreenWord
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabularyNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        const val CHANNEL_ID = "lockscreen_vocabulary_channel_v3"
        const val CHANNEL_NAME = "Vocabulary Reminder"
        const val NOTIFICATION_ID_BASE = 2000
        const val EXTRA_LOCKSCREEN_WORD_ID = "extra_lockscreen_word_id"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.lockscreen_notification_channel_description)
            enableVibration(true)
            setShowBadge(true)
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            setSound(reminderSoundUri(), audioAttributes())
        }

        notificationManager.createNotificationChannel(channel)
    }



    fun show(word: LockScreenWord): Boolean {
        if (!notificationManager.areNotificationsEnabled()) {
             return false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission  = checkSelfPermission(
                context,
                 POST_NOTIFICATIONS
            )
            if (permission !=  PERMISSION_GRANTED) {
                 return false
            }
        }

        createChannel()

         val openIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRA_LOCKSCREEN_WORD_ID, word.id)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            word.id,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

         val goIntent = PendingIntent.getActivity(
            context,
            word.id + 10_000,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell_icon)
            .setContentTitle(word.word)
            .setContentText(word.translation)
             .setStyle(NotificationCompat.BigTextStyle().bigText(word.translation))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .addAction(
                R.drawable.ic_learning_language,
                context.getString(R.string.lockscreen_notification_action_go),
                goIntent
            )
            .setSound(reminderSoundUri())
            .build()

        notificationManager.notify(NOTIFICATION_ID_BASE, notification)
        return true
    }

    fun cancel() {
        notificationManager.cancel(NOTIFICATION_ID_BASE)
    }

    fun cancelAll() {
        notificationManager.cancelAll()
    }

    private fun reminderSoundUri(): Uri {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }

    private fun audioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    }
}
