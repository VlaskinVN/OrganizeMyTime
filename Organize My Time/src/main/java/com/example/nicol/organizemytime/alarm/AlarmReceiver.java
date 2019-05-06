package com.example.nicol.organizemytime.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.nicol.organizemytime.MainActivity;
import com.example.nicol.organizemytime.MyApplication;
import com.example.nicol.organizemytime.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        long timeStamp = intent.getLongExtra("time_stamp", 0);
        String priority = intent.getStringExtra("priority");
        int color = intent.getIntExtra("color", 0);

        Intent intentRes = new Intent(context, MainActivity.class);

        if (MyApplication.isActivityVisible()){
            intentRes = intent;
        }

        intentRes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) timeStamp, intentRes, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Напоминание : " + title);
        builder.setContentText(title);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description).setBigContentTitle("Подробнее о задачи : " + title).setSummaryText(priority));
        builder.setColor(context.getResources().getColor(color));

        builder.setSmallIcon(R.drawable.ic_add_alert);

        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) timeStamp, notification);
        Log.d("=== AR : ", "NOTIFY!!");
    }
}
