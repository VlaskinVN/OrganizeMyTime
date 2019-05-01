package com.example.nicol.organizemytime.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.Utils;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.Calendar;

public class AddingTaskDialogFragment extends DialogFragment {

    public interface AddingTaskListener {
        void onTaskAdded(ModelTask newTask);
        void onTaskAddingCancel();
    }

    private AddingTaskListener addingTaskListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            addingTaskListener = (AddingTaskListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implements AddingTaskListener");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View containte = inflater.inflate(R.layout.dialog_task, null);

        final TextInputLayout tilTitle = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskTitle);
        final EditText editTitle = tilTitle.getEditText();

        final TextInputLayout tilDecription = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskDescription);
        final EditText editDescription = tilDecription.getEditText();

        TextInputLayout tilDate = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskDate);
        final EditText editDate = tilDate.getEditText();

        TextInputLayout tilTime = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskTime);
        final EditText editTime = tilTime.getEditText();

        Spinner spPriority = (Spinner) containte.findViewById(R.id.spDialogTaskPriority);

        tilDate.setHint(getResources().getString(R.string.task_date));
        tilTime.setHint(getResources().getString(R.string.task_time));
        tilTitle.setHint(getResources().getString(R.string.task_title));
        tilDecription.setHint(getResources().getString(R.string.task_description));

        builder.setView(containte);

        final ModelTask task = new ModelTask();

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ModelTask.PRIORITY_LEVELS);
        spPriority.setAdapter(priorityAdapter);

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDate.length() == 0){
                    editDate.setText(" ");
                }
                Calendar dateCalendate = Calendar.getInstance();
                DialogFragment datePickerFragment = DatePickerFragment.newInstance(getActivity(), R.id.tvTaskDate, dateCalendate);
                ((DatePickerFragment) datePickerFragment).setDateDialogFragmentListener(new DateDialogFragmentListener() {
                    @Override
                    public void dateDialogFragmentDateSet(Calendar date) {
                        editDate.setText(Utils.getDate(date.getTimeInMillis()));
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
                Calendar dateCalendate = Calendar.getInstance();
                DialogFragment timePickerFragment = TimePickerFragment.newInstance(getActivity(), R.id.tvTaskDate, dateCalendate);
                ((TimePickerFragment) timePickerFragment).setTimeDialogFragmentListener(new TimeDialogFragmentListener() {
                    @Override
                    public void timeDialogFragmentDateSet(Calendar date) {
                        editTime.setText(Utils.getTime(date.getTimeInMillis()));
                    }
                });

                timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
            }
        });

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.setTitle(editTitle.getText().toString());
                task.setDescription(editDescription.getText().toString());
                if (editDate.length() != 0 || editTime.length() != 0){
                    task.setDate(calendar.getTimeInMillis());
                }
                task.setStatus(ModelTask.STATUS_CURRENT);
                addingTaskListener.onTaskAdded(task);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addingTaskListener.onTaskAddingCancel();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (editTitle.length() == 0){
                    positiveButton.setEnabled(false);
                    tilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                }

                editTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0){
                            positiveButton.setEnabled(false);
                            tilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                        }else{
                            positiveButton.setEnabled(true);
                            tilTitle.setErrorEnabled(false);
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
