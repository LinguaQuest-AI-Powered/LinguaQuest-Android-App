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
import com.iti.linguaquest.features.lockscreen.notification.VocabularyGotItReceiver.Companion.ACTION_GOT_IT
import com.iti.linguaquest.features.lockscreen.notification.VocabularyGotItReceiver.Companion.EXTRA_NOTIFICATION_ID
import com.iti.linguaquest.features.lockscreen.notification.VocabularyGotItReceiver.Companion.EXTRA_WORD_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import com.iti.linguaquest.core.utils.LocaleUtils
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (channel?.importance == NotificationManager.IMPORTANCE_NONE) {
                return false
            }
        }

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

        val gotItIntent = PendingIntent.getBroadcast(
            context,
            word.id + 10_000,
            Intent(context, VocabularyGotItReceiver::class.java).apply {
                action = ACTION_GOT_IT
                putExtra(EXTRA_WORD_ID, word.id)
                putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_ID_BASE)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val locContext = LocaleUtils.wrapContext(context)
        val compactView = buildCompactView(word, contentIntent, gotItIntent, locContext)
        val expandedView = buildExpandedView(word, contentIntent, gotItIntent, locContext)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell_icon)
            .setContentTitle(word.word)
            .setContentText(word.translation)
            .setCustomContentView(compactView)
            .setCustomBigContentView(expandedView)
            .setCustomHeadsUpContentView(expandedView)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setDeleteIntent(gotItIntent)
            .setSound(reminderSoundUri())

        notificationManager.cancel(NOTIFICATION_ID_BASE)
        notificationManager.notify(NOTIFICATION_ID_BASE, builder.build())
        return true
    }

    fun cancel() {
        notificationManager.cancel(NOTIFICATION_ID_BASE)
    }

    fun cancelAll() {
        notificationManager.cancelAll()
    }

    private fun buildCompactView(
        word: LockScreenWord,
        openIntent: PendingIntent,
        gotItIntent: PendingIntent,
        locContext: Context
    ): RemoteViews {
        return RemoteViews(context.packageName, R.layout.layout_lockscreen_notification).apply {
            setTextViewText(R.id.tv_language, word.nativeLanguage.ifBlank { "العربية" })
            setTextViewText(R.id.tv_word_title, word.word)
            setTextViewText(R.id.tv_word_definition, word.meaning.ifBlank { word.translation })
            
            val difficultyStr = when (word.difficulty.lowercase()) {
                "beginner", "easy", "سهل", "مبتدئ" -> locContext.getString(R.string.easy)
                "intermediate", "medium", "متوسط" -> locContext.getString(R.string.medium)
                "advanced", "hard", "صعب", "متقدم" -> locContext.getString(R.string.hard)
                else -> word.difficulty
            }
            setTextViewText(R.id.btn_action, difficultyStr)
            
            setOnClickPendingIntent(R.id.root, openIntent)
        }
    }

    private fun buildExpandedView(
        word: LockScreenWord,
        openIntent: PendingIntent,
        gotItIntent: PendingIntent,
        locContext: Context
    ): RemoteViews {
        return RemoteViews(context.packageName, R.layout.notification_lockscreen_expanded).apply {
            setTextViewText(R.id.tv_language, word.nativeLanguage.ifBlank { "العربية" })
            setTextViewText(R.id.tvWord, word.word)
            setTextViewText(R.id.tvTranslation, word.meaning.ifBlank { word.translation })
            
            if (word.exampleSentence.isNotBlank()) {
                setTextViewText(R.id.tvExample, locContext.getString(R.string.lockscreen_notification_example_label, word.exampleSentence))
            } else {
                setTextViewText(R.id.tvExample, "")
            }
            
            val difficultyStr = when (word.difficulty.lowercase()) {
                "beginner", "easy", "سهل", "مبتدئ" -> locContext.getString(R.string.easy)
                "intermediate", "medium", "متوسط" -> locContext.getString(R.string.medium)
                "advanced", "hard", "صعب", "متقدم" -> locContext.getString(R.string.hard)
                else -> word.difficulty
            }
            setTextViewText(R.id.btn_action, difficultyStr)
            
            setTextViewText(R.id.tv_app_name, locContext.getString(R.string.app_name))
            setTextViewText(R.id.tvHint, locContext.getString(R.string.lockscreen_notification_tap_to_open))
            
            setOnClickPendingIntent(R.id.root_expanded, openIntent)
        }
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
