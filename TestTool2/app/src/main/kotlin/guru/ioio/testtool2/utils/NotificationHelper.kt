package guru.ioio.testtool2.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import guru.ioio.testtool2.PanelActivity
import guru.ioio.testtool2.R

class NotificationHelper {
    companion object {
        val GROUP_MAX = "guru.ioio.testtool2.notification_max"
        val GROUP_MIN = "guru.ioio.testtool2.notification_min"
        val CHANNEL_MAX = "guru.ioio.testtool2.notification_channel_max"
        val CHANNEL_MIN = "guru.ioio.testtool2.notification_channel_min"
    }

    private val mManager: NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context) {
        mManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mManager.createNotificationChannelGroup(NotificationChannelGroup(GROUP_MAX, "最高通知"))
        val channelMax =
            NotificationChannel(CHANNEL_MAX, "最高通知内容", NotificationManager.IMPORTANCE_HIGH)
                .apply {
                    group = GROUP_MAX
                    setShowBadge(true)
                    lightColor = Color.RED
                    enableLights(true)
                    enableVibration(true)
                    setBypassDnd(true)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
        mManager.createNotificationChannel(channelMax)
        mManager.createNotificationChannelGroup(NotificationChannelGroup(GROUP_MIN, "最低通知"))
        val channelMin =
            NotificationChannel(CHANNEL_MIN, "最低通知内容", NotificationManager.IMPORTANCE_MIN)
                .apply {
                    group = GROUP_MIN
                    setShowBadge(true)
                    lightColor = Color.GREEN
                    enableLights(true)
                    enableVibration(true)
                    setBypassDnd(true)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
        mManager.createNotificationChannel(channelMin)
    }

    fun sendWithChannel(context: Context, channel: String, notifyId: Int) {
        showNotifications(
            context,
            createPushIntent(context),
            channel,
            notifyId,
            "通知:$channel",
            "内容:$channel",
            "ticker:$channel"
        )
    }

    fun sendLater(context: Context, notifyId: Int, isLater: Boolean) {
        showNotificationsLater(
            context, CHANNEL_MIN, notifyId,
            "通知：$isLater",
            "内容：$isLater",
            "ticker:$isLater",
            isLater
        )
    }

    private fun showNotificationsLater(
        context: Context, channelId: String, notifyId: Int,
        title: String, content: String, ticker: String, isLater: Boolean
    ) {
        val contentItent = PendingIntent.getActivity(
            context, notifyId, createPushIntent(context),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val builder = NotificationCompat.Builder(context, channelId).apply {
            if (isLater) {
                setWhen(System.currentTimeMillis() + 20L * 24 * 3600 * 1000)
            }
            setSmallIcon(R.drawable.ic_launcher_background)
            setTicker(ticker)
            setContentTitle(title)
            setContentText(content)
            setContentIntent(contentItent)
            setAutoCancel(true)
        }
        val notification = builder.build()
        mManager.notify(notifyId, notification)
    }

    private fun createPushIntent(context: Context): Intent {
        return Intent(context, PanelActivity::javaClass.javaClass)
    }

    private fun showNotifications(
        context: Context,
        intent: Intent,
        channelId: String,
        notifyId: Int,
        title: String,
        content: String,
        tickerText: String
    ) {
        // 提示标题 FIXME zhuyanlin 根据主标题显示提示
        var comTitles = content
        if (!TextUtils.isEmpty(tickerText)) {
            comTitles = tickerText
        }
        // 设置通知的事件消息
        val contentTitle: CharSequence = title // 通知栏标题
        val contentText: CharSequence? = content // 通知栏内容
        val contentIntent =
            PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val mBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setTicker(comTitles)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setContentIntent(contentIntent)
        val notificationCompat: Notification = mBuilder.build()
        mManager.notify(notifyId, notificationCompat)
    }
}