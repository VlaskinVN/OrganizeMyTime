package com.example.nicol.organizemytime.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.nicol.organizemytime.R;

import java.util.Date;

public class ModelTask implements Item, Parcelable {
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    public static final String[] PRIORITY_LEVELS = { "Низкий приоритет", "Обычный приоритет", "Высокий приоритет" };
    public static final String[] STATUS_LEVELS = { "Просроченно", "Запланированно", "Выполненно" };

    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;

    private String title;
    private String description;
    private long date;
    private int priority;
    private int status;
    private long timeStamp;
    private int dateStatus;
    private String mapCoordinate;

    public ModelTask(){
        status = -1;
        this.timeStamp = new Date().getTime();
    }

    public ModelTask(String title, long date, int priority, int status, String description, long timeStamp, String mapCoordinate){
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.description = description;
        this.timeStamp = timeStamp;
        this.mapCoordinate = mapCoordinate;
    }

    public ModelTask(Parcel source) {
        this.title = source.readString();
        this.description = source.readString();
        this.date = source.readLong();
        this.priority = source.readInt();
        this.status = source.readInt();
        this.timeStamp = source.readLong();
    }

    public int getPriorityColor(){
        switch (getPriority()){
            case PRIORITY_HIGH:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE){
                    return R.drawable.priority_rect_high;
                }else{
                    return R.drawable.priority_rect_high_selected;
                }
            case PRIORITY_NORMAL:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE){
                    return R.drawable.priority_rect_normal;
                }else{
                    return R.drawable.priority_rect_normal_selected;
                }
            case PRIORITY_LOW:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE){
                    return R.drawable.priority_rect_low;
                }else{
                    return R.drawable.priority_rect_low_selected;
                }
                default: return 0;
        }
    }

    @Override
    public boolean isTask() {
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(int dateStatus) {
        this.dateStatus = dateStatus;
    }

    public String getMapCoordinate() {
        return mapCoordinate;
    }

    public void setMapCoordinate(String mapCoordinate) {
        this.mapCoordinate = mapCoordinate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeLong(this.date);
        dest.writeInt(this.priority);
        dest.writeInt(this.status);
        dest.writeLong(this.timeStamp);
    }

    public static final Parcelable.Creator<ModelTask> CREATOR = new Parcelable.Creator<ModelTask>(){

        @Override
        public ModelTask createFromParcel(Parcel source) {
            return new ModelTask(source);
        }

        @Override
        public ModelTask[] newArray(int size) {
            return new ModelTask[size];
        }
    };
}
