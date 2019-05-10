package com.example.nicol.organizemytime.alarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.example.nicol.organizemytime.MainActivity;
import com.example.nicol.organizemytime.MyApplication;
import com.example.nicol.organizemytime.R;

public class AlarmService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmService(String name) {
        super(name);
    }

    public AlarmService() {
        super(AlarmService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        long timeStamp = intent.getLongExtra("time_stamp", 0);
        String priority = intent.getStringExtra("priority");

        Intent intentRes = new Intent(this, MainActivity.class);

        if (MyApplication.isActivityVisible()){
            intentRes = intent;
        }

        intentRes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) timeStamp, intentRes, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Напоминание : " + title);
        builder.setContentText(title);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description).setBigContentTitle("Подробнее о задачи : " + title).setSummaryText(priority));
        //builder.setColor(this.getResources().getColor(color));

        builder.setSmallIcon(R.drawable.ic_add_alert);

        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify((int) timeStamp, notification);
    }
}
