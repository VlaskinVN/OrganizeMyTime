package com.example.nicol.organizemytime.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.example.nicol.organizemytime.model.ModelTask;

@TargetApi(21)
public class AlarmHelper {
    private static AlarmHelper instance;
    private Context context;
    private AlarmManager alarmManager;

    public static AlarmHelper getInstance(){
        if (instance == null){
            instance = new AlarmHelper();
        }

        return instance;
    }

    public void init(Context context){
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(ModelTask task){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("time_stamp", task.getTimeStamp());
        intent.putExtra("description", (task.getDescription().length() <= 0)?"Без описания.":task.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);
    }

    public void removeAlarm(long taskTimeStamp){
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) taskTimeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
