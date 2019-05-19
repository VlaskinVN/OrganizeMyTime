package com.example.nicol.organizemytime.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.Utils;
import com.example.nicol.organizemytime.model.ModelTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", task);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);
    }

    public void removeAlarm(long taskTimeStamp){
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) taskTimeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    public void setAlarmEveryday(ModelTask task) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", task);
        intent.putExtras(bundle);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(task.getDate());

        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);

        Log.d("=== AH ", h + " : " + m);

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
            task.setDate(calendar.getTimeInMillis());
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void setAlarmOptionUser(ModelTask task) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", task);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String repeatDay = task.getRepeatDays();
        String[] arrday = repeatDay.split(",");
        repeatDay.trim();

        String[] days = {"", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};

        for (int i = 0; i < arrday.length - 1; i++){
            Log.d("=== AH ", arrday[i].trim());
            String str = arrday[i].trim();
            for (int j = 0; j < days.length; j++){
                if (str.equals(days[i])){
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(task.getDate());

                    switch (j){
                        case 1:
                            calendar.set(Calendar.DAY_OF_WEEK, 1);
                            task.setDate(calendar.getTimeInMillis());
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            break;
                        case 2:
                            calendar.set(Calendar.DAY_OF_WEEK, 2);
                            task.setDate(calendar.getTimeInMillis());
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            break;
                        case 3:
                            calendar.set(Calendar.DAY_OF_WEEK, 3);
                            task.setDate(calendar.getTimeInMillis());
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            break;
                        case 4:
                            calendar.set(Calendar.DAY_OF_WEEK, 4);
                            task.setDate(calendar.getTimeInMillis());
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            break;
                        case 5:
                            calendar.set(Calendar.DAY_OF_WEEK, 5);
                            task.setDate(calendar.getTimeInMillis());
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            break;
                        case 6:
                            calendar.set(Calendar.DAY_OF_WEEK, 6);
                            task.setDate(calendar.getTimeInMillis());
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            break;
                        case 7:
                            calendar.set(Calendar.DAY_OF_WEEK, 7);
                            task.setDate(calendar.getTimeInMillis());
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            break;
                    }
                }

            }
        }

    }
}
