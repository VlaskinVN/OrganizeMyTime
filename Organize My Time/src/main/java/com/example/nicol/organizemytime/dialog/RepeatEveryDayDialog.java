package com.example.nicol.organizemytime.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.Utils;

import java.util.Calendar;

public class RepeatEveryDayDialog extends DialogFragment {
    RepeatEverydayDialogListener redl;

    public static RepeatEveryDayDialog newInstance(){
        RepeatEveryDayDialog dialog = new RepeatEveryDayDialog();

        return dialog;
    }

    public void setRepeatDayDialogListener(RepeatEverydayDialogListener redl){
        this.redl = redl;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title_repeat_days);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View containte = inflater.inflate(R.layout.dialog_task_repeat_everyday, null);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                redl.setRepeatEverydayListener(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            }
        });

        TextInputLayout tilTime = (TextInputLayout) containte.findViewById(R.id.tilDialogTime);
        final EditText editTime = tilTime.getEditText();
        tilTime.setHint(getResources().getString(R.string.task_time));

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTime.length() == 0){
                    editTime.setText(" ");
                }
                DialogFragment timePickerFragment = TimePickerFragment.newInstance(getActivity(), R.id.tvTaskDate, calendar);
                ((TimePickerFragment) timePickerFragment).setTimeDialogFragmentListener(new TimeDialogFragmentListener() {
                    @Override
                    public void timeDialogFragmentDateSet(int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

                        editTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                    }
                });

                timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
            }
        });


        builder.setView(containte);

        return builder.create();
    }
}
