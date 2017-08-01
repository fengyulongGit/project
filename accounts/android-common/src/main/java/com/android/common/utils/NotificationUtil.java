package com.android.common.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationUtil {
    public static void notify(Context context, Notification notification) {
        getNotificationManager(context).notify(0, notification);
    }

    public static void notifyCancel(Context context) {
        getNotificationManager(context).cancel(0);
    }

    private static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static Notification makeNotification(Context context, int icon, long when, Intent notificationIntent,
                                                String tickerText, String contentTitle, String contentText) {
        // 设置通知的事件消息
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // 用上面的属性初始化 Nofification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(icon)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setTicker(tickerText)
                .setWhen(when);

        Notification notification = builder.build();
        // 添加声音
        notification.defaults |= Notification.DEFAULT_SOUND;
        // 添加振动
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        // 添加LED灯提醒
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        // 将通知放到通知栏的“正在运行”组中
        // notification.flags |= Notification.FLAG_ONGOING_EVENT;
        // 标明在点击了通知栏中的“清除通知”后，此通知不清除
        // notification.flags |= Notification.FLAG_NO_CLEAR;

        return notification;
    }

/*	public static Notification makeNotification(Context context, int icon, long when, Intent notificationIntent,
            String tickerText, String contentTitle, String contentText) {
		// 用上面的属性初始化 Nofification
		Notification notification = new Notification(icon, tickerText, when);
		// 添加声音
		notification.defaults |= Notification.DEFAULT_SOUND;
		// 添加振动
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		// 添加LED灯提醒
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		// 将通知放到通知栏的“正在运行”组中
		// notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// 标明在点击了通知栏中的“清除通知”后，此通知不清除
		// notification.flags |= Notification.FLAG_NO_CLEAR;

		// 设置通知的事件消息
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		return notification;
	}*/
}
