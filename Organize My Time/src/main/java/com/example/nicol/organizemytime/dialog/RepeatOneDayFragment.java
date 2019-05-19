package com.example.nicol.organizemytime.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.Utils;

import java.util.Calendar;

public class RepeatOneDayFragment extends DialogFragment {

    static RepeatOneDayFragment dialog;
    RepeatOneDayDialogListener roddl;

    public static RepeatOneDayFragment newInstance(){
        dialog = new RepeatOneDayFragment();

        return dialog;
    }

    public void setRepeatOneDayDialogListener(RepeatOneDayDialogListener roddl){
        this.roddl = roddl;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title_repeat_days);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View containte = inflater.inflate(R.layout.dialog_task_repeat_oneday, null);

        final TextInputLayout tilDate = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskDate);
        final EditText editDate = tilDate.getEditText();
        TextInputLayout tilTime = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskTime);
        final EditText editTime = tilTime.getEditText();

        tilDate.setHint(getResources().getString(R.string.task_date));
        tilTime.setHint(getResources().getString(R.string.task_time));

        final Calendar calendar = Calendar.getInstance();

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roddl.setRepeatOneDayListener(calendar);
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDate.length() == 0){
                    editDate.setText(" ");
                }

                DialogFragment datePickerFragment = DatePickerFragment.newInstance(getActivity(), R.id.tvTaskDate, calendar);
                ((DatePickerFragment) datePickerFragment).setDateDialogFragmentListener(new DateDialogFragmentListener() {
                    @Override
                    public void dateDialogFragmentDateSet(int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        editDate.setText(Utils.getDate(calendar.getTimeInMillis()));
                    }
                });

                datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
            }
        });

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
        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                          @Override
                                          public void onShow(DialogInterface dialog) {
                                              final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                                              editDate.addTextChangedListener(new TextWatcher() {
                                                  @Override
                                                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                  }

                                                  @Override
                                                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                      if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR) ||
                                                              calendar.get(Calendar.MONTH) < Calendar.getInstance().get(Calendar.MONTH) ||
                                                              calendar.get(Calendar.YEAR) < Calendar.getInstance().get(Calendar.YEAR)) {
                                                          positiveButton.setEnabled(false);
                                                          tilDate.setError("Выбранна некоректная дата!");
                                                      } else {
                                                          positiveButton.setEnabled(true);
                                                          tilDate.setErrorEnabled(false);
                                                      }
                                                  }

                                                  @Override
                                                  public void afterTextChanged(Editable s) {

                                                  }
                                              });
                                          }
                                      });




        return alertDialog;
    }
}
