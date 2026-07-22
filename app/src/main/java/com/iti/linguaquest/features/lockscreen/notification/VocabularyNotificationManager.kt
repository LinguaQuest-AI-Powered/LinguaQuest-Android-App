package com.iti.linguaquest.features.lockscreen.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
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
        const val CHANNEL_ID = "lockscreen_vocabulary_channel"
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
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Periodic lock screen vocabulary reminders"
            enableVibration(true)
            setSound(reminderSoundUri(), audioAttributes())
        }

        notificationManager.createNotificationChannel(channel)
    }

    fun show(word: LockScreenWord) {
        createChannel()

        val heroImage = BitmapFactory.decodeResource(context.resources, R.drawable.lingo_reward)
        val contentIntent = PendingIntent.getActivity(
            context,
                word.id,
                Intent(context, MainActivity::class.java).apply {
                    putExtra(EXTRA_LOCKSCREEN_WORD_ID, word.id)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell_icon)
            .setContentTitle(word.word)
            .setContentText(word.translation)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${word.translation}\n\n${word.exampleSentence}")
                    .setBigContentTitle(word.word)
            )
            .setLargeIcon(heroImage)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setSound(reminderSoundUri())
            .build()

        notificationManager.notify(NOTIFICATION_ID_BASE + word.id, notification)
    }

    fun cancel(wordId: Int) {
        notificationManager.cancel(NOTIFICATION_ID_BASE + wordId)
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
