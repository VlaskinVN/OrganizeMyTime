package com.example.nicol.organizemytime.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicol.organizemytime.model.ModelTask;

public class DbUpdateManager {
    private SQLiteDatabase db;

    DbUpdateManager(SQLiteDatabase db){
        this.db = db;
    }

    public void titleUpdate(long timeStamp, String title){
        update(DbHelper.DB_TASK_TITLE, timeStamp, title);
    }

    public void dateUpdate(long timeStamp, long date){
        update(DbHelper.DB_TASK_DATE, timeStamp, date);
    }

    public void priorityUpdate(long timeStamp, int priority){
        update(DbHelper.DB_TASK_PRIORITY, timeStamp, priority);
    }

    public void statusUpdate(long timeStamp, int status){
        update(DbHelper.DB_TASK_STATUS, timeStamp, status);
    }

    public void descriptionUpdate(long timeStamp, String description){
        update(DbHelper.DB_TASK_DESCRIPTION, timeStamp, description);
    }

    public void mapCoordinateUpdate(long timeStamp, String mapCoordinate){
        update(DbHelper.DB_TASK_MAP_COORDINATE, timeStamp, mapCoordinate);
    }

    public void repeatDaysUpdate(long timeStamp, String repeatDay){
        update(DbHelper.DB_TASK_REPEAT_DAYS, timeStamp, repeatDay);
    }

    public void taskUpdate(ModelTask task){
        titleUpdate(task.getTimeStamp(), task.getTitle());
        dateUpdate(task.getTimeStamp(), task.getDate());
        priorityUpdate(task.getTimeStamp(), task.getPriority());
        statusUpdate(task.getTimeStamp(), task.getStatus());
        descriptionUpdate(task.getTimeStamp(), task.getDescription());
        mapCoordinateUpdate(task.getTimeStamp(), task.getMapCoordinate());
        repeatDaysUpdate(task.getTimeStamp(), task.getRepeatDays());
    }

    private void update(String column, long key, String value){
        ContentValues cv = new ContentValues();
        cv.put(column, value);

        db.update(DbHelper.DB_TABLE, cv, DbHelper.DB_TASK_TIMESTAMP + " = " + key, null);
    }

    private void update(String column, long key, long value){
        ContentValues cv = new ContentValues();
        cv.put(column, value);

        db.update(DbHelper.DB_TABLE, cv, DbHelper.DB_TASK_TIMESTAMP + " = " + key, null);
    }
}
