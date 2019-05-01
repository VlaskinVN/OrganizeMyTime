package com.example.nicol.organizemytime;

import com.example.nicol.organizemytime.model.ModelTask;

import java.text.SimpleDateFormat;

public class Utils {
    public static String getDate(long date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        return dateFormat.format(date);
    }

    public static String getTime(long time){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm");
        return timeFormat.format(time);
    }

    public static String getFullDate(long date){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy  HH.mm");
        return format.format(date);
    }

    public static String getPriorityType(int idType){
        return ModelTask.PRIORITY_LEVELS[idType];
    }

    public static String getStatusType(int idStatus){
        return ModelTask.STATUS_LEVELS[idStatus];
    }
}
