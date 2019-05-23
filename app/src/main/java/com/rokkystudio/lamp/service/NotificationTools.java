package com.rokkystudio.lamp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.rokkystudio.lamp.MainActivity;
import com.rokkystudio.lamp.R;

public class NotificationTools
{
    private static final int NOTIFY_ID = 100;
    private Service mService;

    public NotificationTools(Service service) {
        mService = service;
    }

    public void showNotification()
    {
        Intent notificationIntent = new Intent(mService, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mService, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Resources resources = mService.getResources();

        RemoteViews remoteViews = new RemoteViews(mService.getPackageName(), R.layout.notification);
        ComponentName component = new ComponentName(mService, LampService.class);

        //setClickPending(remoteViews, R.id.notification_button_play_pause, LampService.PLAY_PAUSE, component);
        //setClickPending(remoteViews, R.id.notification_button_stop_track, LampService.STOP_TRACK, component);
        //setClickPending(remoteViews, R.id.notification_button_prev_track, LampService.PREV_TRACK, component);
        //setClickPending(remoteViews, R.id.notification_button_next_track, LampService.NEXT_TRACK, component);
        //remoteViews.setTextViewText(R.id.notification_text_title, title);

        // Apply the layouts to the notification
        Notification customNotification = new NotificationCompat.Builder(mService)
                .setSmallIcon(R.drawable.notification_icon)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews)
                .build();

        NotificationManager notificationManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);
        // Альтернативный вариант
        // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID, customNotification);
    }

    private void setClickPending(RemoteViews remoteViews, int buttonId, String action, ComponentName componentName) {
        Intent intent = new Intent(action);
        intent.setComponent(componentName);
        remoteViews.setOnClickPendingIntent(buttonId, PendingIntent.getService(mService, 1, intent, 0));
    }

    public void hideNotification() {

    }
}
