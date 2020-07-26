package com.moapp.meet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.moapp.meet.adapter.CheckChatListAdapter;

//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.NotificationCompat;

public class AlarmService extends Service {


    CheckChatListAdapter cc = new CheckChatListAdapter();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId =  createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .build();

            startForeground(1, notification);
        }


        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelID="channel_01";
            String channelName="MyChannel01";

            NotificationChannel channel= new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(getApplicationContext(), channelID);

        }else{
            builder= new NotificationCompat.Builder(getApplicationContext(), null);
        }

        builder.setSmallIcon(R.raw.talk);


        builder.setContentTitle("모임 시간이 되었습니다");
        builder.setContentText( "모임 시간이 되었습니다.");
        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.raw.talk);
        builder.setLargeIcon(bm);



        Intent intent1 = new Intent(getApplicationContext(),CheckChatListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity (getApplicationContext(), 0 , intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        Uri soundUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION);
        soundUri =Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.click);
        builder.setSound(soundUri);

        builder.setVibrate(new long[]{0,500,0,0});

        Notification notification=builder.build();

        notificationManager.notify(1, notification);


        Toast.makeText(getApplicationContext(),"모임 시간이 되었습니다", Toast.LENGTH_SHORT).show();





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        stopSelf();

        return START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "Alarm";
        String channelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        //channel.setDescription(channelName);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return channelId;
    }


}
