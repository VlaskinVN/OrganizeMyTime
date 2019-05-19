package com.example.nicol.organizemytime.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.nicol.organizemytime.Utils;
import com.example.nicol.organizemytime.database.DbHelper;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DbHelper dbHelper = new DbHelper(context);

        AlarmHelper.getInstance().init(context);
        AlarmHelper alarmHelper = AlarmHelper.getInstance();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(dbHelper.getQueryManager().getTasks(DbHelper.DB_SELECTION_STATUS
                        + " OR "
                        + DbHelper.DB_SELECTION_STATUS,
                new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                        Integer.toString(ModelTask.STATUS_OVERDUE)},
                DbHelper.DB_TASK_DATE
        ));

        for (ModelTask task : tasks) {
            if (task.getDate() != 0){
                Log.d("=== AS ", "----------------");
                alarmHelper.setAlarm(task);
            }
        }

    }
}
