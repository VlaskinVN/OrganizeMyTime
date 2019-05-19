package com.example.nicol.organizemytime.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.nicol.organizemytime.R;

import java.util.Calendar;

public class RepeatDialogFragment extends DialogFragment {

    private RepeatDialogListener rdl;
    static RepeatDialogFragment dialog;

    public static RepeatDialogFragment newInstance(){
        dialog = new RepeatDialogFragment();

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title_repeat_days);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View containte = inflater.inflate(R.layout.dialog_task_repeat, null);

        Button btnOneDay = (Button) containte.findViewById(R.id.btnOneDay);
        Button btnEveryday = (Button) containte.findViewById(R.id.btnEveryday);
        Button btnOptionUser = (Button) containte.findViewById(R.id.btnOptionUser);

        btnOneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DialogFragment rep = RepeatOneDayFragment.newInstance();
                ((RepeatOneDayFragment) rep).setRepeatOneDayDialogListener(new RepeatOneDayDialogListener() {
                    @Override
                    public void setRepeatOneDayListener(Calendar calendar) {
                        rdl.repeatOneDayListener(calendar);
                    }
                });
                rep.show(getFragmentManager(), "RepeatOneDayFragment");
            }
        });

        btnEveryday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                DialogFragment rep = RepeatEveryDayDialog.newInstance();
                ((RepeatEveryDayDialog) rep).setRepeatDayDialogListener(new RepeatEverydayDialogListener() {
                    @Override
                    public void setRepeatEverydayListener(int hour, int minute) {
                        rdl.repeatEverydayListener(true, hour, minute);
                    }
                });

                rep.show(getFragmentManager(), "RepeatEveryDayDialog");
            }
        });

        btnOptionUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DialogFragment rep = RepeatUserSelectionDialogFragment.newInstance();
                ((RepeatUserSelectionDialogFragment) rep).setRepeatDayDialogListener(new RepeatDayDialogListener() {
                    @Override
                    public void setRepeatDayListener(String days, int hour, int minute) {
                        rdl.repeatOptionUserListener(days, hour, minute);
                    }
                });

                rep.show(getFragmentManager(), "RepeatUserSelectionDialogFragment");
            }
        });

        builder.setView(containte);

        return builder.create();
    }

    public void setRepeadListeners(RepeatDialogListener rdl){
        this.rdl = rdl;
    }
}
