package com.example.nicol.organizemytime.model;

import com.example.nicol.organizemytime.R;

public class ModelSeparator implements Item {
    public static final int SEP_OVERDUE = R.string.sep_overdue;
    public static final int SEP_TODAY = R.string.sep_today;
    public static final int SEP_TOMORROW = R.string.sep_tomorrow;
    public static final int SEP_FUTURE = R.string.sep_future;

    private int type;

    public ModelSeparator(int type){
        this.type = type;
    }


    @Override
    public boolean isTask() {
        return false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
