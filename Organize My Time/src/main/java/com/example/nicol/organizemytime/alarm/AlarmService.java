package com.example.nicol.organizemytime.alarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.example.nicol.organizemytime.MainActivity;
import com.example.nicol.organizemytime.MyApplication;
import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.ReadDialogActivity;
import com.example.nicol.organizemytime.model.ModelTask;

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
        Bundle bundle = intent.getExtras();
        ModelTask task = bundle.getParcelable("data");

        String title = task.getTitle();
        String description = task.getDescription();
        long timeStamp = task.getTimeStamp();
        String priority = ModelTask.PRIORITY_LEVELS[task.getPriority()];

        Intent intentRes = new Intent(this, MainActivity.class);

        if (MyApplication.isActivityVisible()){
            intentRes = intent;
        }

        intentRes.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) timeStamp, intentRes, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent readIntent = new Intent(this, ReadDialogActivity.class);
        readIntent.putExtras(bundle);

        PendingIntent readPIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), readIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Напоминание : " + title);
        builder.setContentText(title);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description).setBigContentTitle("Подробнее о задачи : " + title).setSummaryText(priority));
        builder.addAction(R.drawable.ic_add, "Просмотреть", readPIntent);
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
