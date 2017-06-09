package com.next.firebasepushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by next on 1/4/17.
 */
public class MyFireBaseMessagingService extends FirebaseMessagingService
{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
       /* Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder noBuilder= new NotificationCompat.Builder(this);
        noBuilder.setContentTitle("FRom FireBAse");
        noBuilder.setContentText(remoteMessage.getNotification().getBody());
        noBuilder.setAutoCancel(true);
        noBuilder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,noBuilder.build());*/
        Log.i("notification",remoteMessage.getNotification().getBody());
        Toast.makeText(this,""+remoteMessage.getNotification().getBody(),Toast.LENGTH_LONG).show();

    }


    @Override
    protected Intent zzaa(Intent intent)
    {
        return null;
    }
}
