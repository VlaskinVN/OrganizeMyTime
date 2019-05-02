package com.example.nicol.organizemytime.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nicol.organizemytime.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

public class DbQueryManager {
    private SQLiteDatabase db;

    DbQueryManager(SQLiteDatabase db){
        this.db = db;
    }

    public ModelTask getTask(long timeStamp){
        ModelTask modelTask = null;
        Cursor c = db.query(DbHelper.DB_TABLE, null, DbHelper.DB_SELECTION_TIMESTAMP,
                    new String[]{Long.toString(timeStamp)}, null, null, null);

        if (c.moveToFirst()){
            String title = c.getString(c.getColumnIndex(DbHelper.DB_TASK_TITLE));
            long date = c.getLong(c.getColumnIndex(DbHelper.DB_TASK_DATE));
            int priority = c.getInt(c.getColumnIndex(DbHelper.DB_TASK_PRIORITY));
            int status = c.getInt(c.getColumnIndex(DbHelper.DB_TASK_STATUS));
            String description = c.getString(c.getColumnIndex(DbHelper.DB_TASK_DESCRIPTION));

            modelTask = new ModelTask(title, date, priority, status, description, timeStamp);
        }
        c.close();
        return modelTask;
    }

    public List<ModelTask> getTasks(String selection, String[] selectionArgs, String orderBy){
        List<ModelTask> tasks = new ArrayList<>();

        Cursor cursor = db.query(DbHelper.DB_TABLE, null, selection, selectionArgs, null, null, orderBy);

        if (cursor.moveToFirst()){
            do {
                String title = cursor.getString(cursor.getColumnIndex(DbHelper.DB_TASK_TITLE));
                long date = cursor.getLong(cursor.getColumnIndex(DbHelper.DB_TASK_DATE));
                long timeStamp = cursor.getLong(cursor.getColumnIndex(DbHelper.DB_TASK_TIMESTAMP));
                int priority = cursor.getInt(cursor.getColumnIndex(DbHelper.DB_TASK_PRIORITY));
                int status = cursor.getInt(cursor.getColumnIndex(DbHelper.DB_TASK_STATUS));
                String description = cursor.getString(cursor.getColumnIndex(DbHelper.DB_TASK_DESCRIPTION));

                tasks.add(new ModelTask(title, date, priority, status, description, timeStamp));
            }while (cursor.moveToNext());
        }
        cursor.close();

        return tasks;
    }
}
