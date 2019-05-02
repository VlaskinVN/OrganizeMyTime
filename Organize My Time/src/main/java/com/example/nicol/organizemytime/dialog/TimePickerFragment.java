package com.example.nicol.organizemytime.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment{
    private static String TAG = "== timePickerFragment";
    static Context sContext;
    static Calendar stime;
    static TimeDialogFragmentListener sListener;

    public static TimePickerFragment newInstance(Context context, int titleResource, Calendar time){
        TimePickerFragment dialog = new TimePickerFragment();
        sContext = context;
        stime = time;

        Bundle args = new Bundle();
        args.putInt("title", titleResource);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new TimePickerDialog(sContext, timeSetListener, stime.get(Calendar.HOUR_OF_DAY), stime.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
    }

    public void setTimeDialogFragmentListener(TimeDialogFragmentListener listener){
        sListener = listener;
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            stime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            stime.set(Calendar.MINUTE, minute);
            stime.set(Calendar.SECOND, 0);
            sListener.timeDialogFragmentDateSet(hourOfDay, minute);
        }
    };    
}
