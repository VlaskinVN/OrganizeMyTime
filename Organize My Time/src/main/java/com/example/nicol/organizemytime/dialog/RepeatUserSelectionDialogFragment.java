package com.example.nicol.organizemytime.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.Utils;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.ArrayList;
import java.util.Calendar;

public class RepeatUserSelectionDialogFragment extends DialogFragment {
    public String getDays() {
        return day;
    }
    public void setDays(String days) {
        this.day = days;
    }

    public String day = "";
    private RepeatDayDialogListener rddl;

    CheckBox cbMon;
    CheckBox cbTue;
    CheckBox cbWen;
    CheckBox cbThu;
    CheckBox cbFri;
    CheckBox cbSat;
    CheckBox cbSun;

    public static RepeatUserSelectionDialogFragment newInstance(){
        RepeatUserSelectionDialogFragment dialog = new RepeatUserSelectionDialogFragment();

        return dialog;
    }

    public void setRepeatDayDialogListener(RepeatDayDialogListener rddls){
        this.rddl = rddls;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> days = new ArrayList<String>();
                if (cbMon.isChecked()){
                    days.add(ModelTask.REPEAT_DAYS[ModelTask.REPEAT_DAY_MONDAY]);
                }
                if (cbTue.isChecked()){
                    days.add(ModelTask.REPEAT_DAYS[ModelTask.REPEAT_DAY_TUESDAY]);
                }
                if (cbWen.isChecked()){
                    days.add(ModelTask.REPEAT_DAYS[ModelTask.REPEAT_DAY_WEDNESDAY]);
                }
                if (cbThu.isChecked()){
                    days.add(ModelTask.REPEAT_DAYS[ModelTask.REPEAT_DAY_THURSDAY]);
                }
                if (cbFri.isChecked()){
                    days.add(ModelTask.REPEAT_DAYS[ModelTask.REPEAT_DAY_FRIDAY]);
                }
                if (cbSat.isChecked()){
                    days.add(ModelTask.REPEAT_DAYS[ModelTask.REPEAT_DAY_SATURDAY]);
                }
                if (cbSun.isChecked()){
                    days.add(ModelTask.REPEAT_DAYS[ModelTask.REPEAT_DAY_SUNDAY]);
                }

                for (int j = 0; j <= days.size() - 1; j++){
                    day += days.get(j) + ", ";
                }

                rddl.setRepeatDayListener(day, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setTitle(R.string.dialog_title_repeat_days);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View containte = inflater.inflate(R.layout.dialog_task_repeat_days, null);

        cbMon = (CheckBox) containte.findViewById(R.id.cbMon);
        cbTue = (CheckBox) containte.findViewById(R.id.cbTue);
        cbWen = (CheckBox) containte.findViewById(R.id.cbWen);
        cbThu = (CheckBox) containte.findViewById(R.id.cbThu);
        cbFri = (CheckBox) containte.findViewById(R.id.cbFri);
        cbSat = (CheckBox) containte.findViewById(R.id.cbSat);
        cbSun = (CheckBox) containte.findViewById(R.id.cbSun);

        TextInputLayout tilTime = (TextInputLayout) containte.findViewById(R.id.tilTime);
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
