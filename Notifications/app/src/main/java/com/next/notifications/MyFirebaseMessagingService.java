package com.next.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by next on 3/4/17.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    String TAG ="FCM";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG," id"+remoteMessage.getMessageId());
        Log.d(TAG,"time"+remoteMessage.getSentTime());
        Log.d(TAG,"type"+remoteMessage.getMessageType());

            Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 01, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)

                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("School Notifications")
                .setContentText(remoteMessage.getNotification().getBody());
        Notification n = builder.build();

        nm.notify(01, n);
    }
}
