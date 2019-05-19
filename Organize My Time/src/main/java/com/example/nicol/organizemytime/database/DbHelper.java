package com.example.nicol.organizemytime.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.nicol.organizemytime.model.ModelTask;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "organize_time_db";
    public static final String DB_TABLE = "tasks_table";

    public static final String DB_TASK_TITLE = "task_title";
    public static final String DB_TASK_DATE = "task_date";
    public static final String DB_TASK_PRIORITY = "task_priority";
    public static final String DB_TASK_STATUS = "task_status";
    public static final String DB_TASK_DESCRIPTION = "task_description";
    public static final String DB_TASK_TIMESTAMP = "task_timestamp";
    public static final String DB_TASK_MAP_COORDINATE = "task_map_coordinate";
    public static final String DB_TASK_REPEAT_DAYS = "task_repeat_days";

    private static final String DB_COMMAND_CREATE_TABLE =
            "CREATE TABLE " + DB_TABLE + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DB_TASK_TITLE + " TEXT NOT NULL, "
            + DB_TASK_DATE + " LONG, "
            + DB_TASK_PRIORITY + " INTEGER, "
            + DB_TASK_STATUS + " INTEGER, "
            + DB_TASK_DESCRIPTION + " TEXT, "
            + DB_TASK_TIMESTAMP + " LONG,"
            + DB_TASK_MAP_COORDINATE + " TEXT,"
            + DB_TASK_REPEAT_DAYS + " TEXT);";

    public static final String DB_SELECTION_STATUS = DbHelper.DB_TASK_STATUS + " = ?";
    public static final String DB_SELECTION_TIMESTAMP = DbHelper.DB_TASK_TIMESTAMP + " = ?";

    private DbQueryManager queryManager;
    private DbUpdateManager updateManager;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        queryManager = new DbQueryManager(getReadableDatabase());
        updateManager = new DbUpdateManager(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_COMMAND_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + DB_TABLE);
        onCreate(db);
    }

    public void saveTask(ModelTask task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_TASK_TITLE, task.getTitle());
        contentValues.put(DB_TASK_DATE, task.getDate());
        contentValues.put(DB_TASK_PRIORITY, task.getPriority());
        contentValues.put(DB_TASK_STATUS, task.getStatus());
        contentValues.put(DB_TASK_DESCRIPTION, task.getDescription());
        contentValues.put(DB_TASK_TIMESTAMP, task.getTimeStamp());
        contentValues.put(DB_TASK_MAP_COORDINATE, task.getMapCoordinate());
        contentValues.put(DB_TASK_REPEAT_DAYS, task.getRepeatDays());

        getWritableDatabase().insert(DB_TABLE, null, contentValues);
    }

    public DbQueryManager getQueryManager() {
        return queryManager;
    }

    public DbUpdateManager getUpdateManager() {
        return updateManager;
    }

    public void removeTask(long timeStamp){
        getWritableDatabase().delete(DB_TABLE, DB_SELECTION_TIMESTAMP, new String[]{Long.toString(timeStamp)});
    }
}
