package com.example.nicol.organizemytime.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.Utils;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    private static String TAG = "== DatePickerFragment";
    static Context sContext;
    static Calendar sDate;
    static DateDialogFragmentListener sListener;

    public static DatePickerFragment newInstance(Context context, int titleResource, Calendar date){
        DatePickerFragment dialog = new DatePickerFragment();
        sContext = context;
        sDate = date;

        Bundle args = new Bundle();
        args.putInt("title", titleResource);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(sContext, dateSetListener, sDate.get(Calendar.YEAR), sDate.get(Calendar.MONTH), sDate.get(Calendar.DAY_OF_MONTH));
    }

    public void setDateDialogFragmentListener(DateDialogFragmentListener listener){
        sListener = listener;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            sDate.set(Calendar.YEAR, year);
            sDate.set(Calendar.MONTH, month);
            sDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            sListener.dateDialogFragmentDateSet(sDate);
        }
    };
}