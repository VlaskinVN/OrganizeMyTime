package com.example.nicol.organizemytime.dialog;

import java.util.Calendar;

interface RepeatDialogListener {
    void repeatOptionUserListener(String day, int hour, int minute);
    void repeatOneDayListener(Calendar calendar);
    void repeatEverydayListener(boolean every, int hour, int minute);
}
