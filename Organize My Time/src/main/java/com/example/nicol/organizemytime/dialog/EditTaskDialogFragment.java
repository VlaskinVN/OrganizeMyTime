package com.example.nicol.organizemytime.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.nicol.organizemytime.alarm.AlarmHelper;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.Calendar;

public class EditTaskDialogFragment extends DialogFragment {

    public static EditTaskDialogFragment newInstance(ModelTask task) {
        EditTaskDialogFragment etdf = new EditTaskDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", task.getTitle());
        args.putLong("date", task.getDate());
        args.putInt("priority", task.getPriority());
        args.putString("description", task.getDescription());
        args.putLong("time_stamp", task.getTimeStamp());

        etdf.setArguments(args);

        return etdf;
    }

    private EditTaskListener editTaskListener;

    public interface EditTaskListener {
        void onTaskEdited(ModelTask updateTask);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            editTaskListener = (EditTaskListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implements EDITTASKLISTENER");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString("title");
        long date = args.getLong("date", 0);
        int priority = args.getInt("priority", 0);
        String description = args.getString("description");
        long timeStamp = args.getLong("time_stamp", 0);
        
        final ModelTask task = new ModelTask(title, date, priority,0, description, timeStamp);        
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_title_edit);

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

        editTitle.setText(task.getTitle());
        editTitle.setSelection(editTitle.length());
        if (task.getDate() != 0){
            editDate.setText(Utils.getDate(task.getDate()));
            editTime.setText(Utils.getTime(task.getDate()));
        }
        editDescription.setText(task.getDescription());

        builder.setView(containte);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ModelTask.PRIORITY_LEVELS);
        spPriority.setAdapter(priorityAdapter);

        spPriority.setSelection(task.getPriority());

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
        if (editDate.length() != 0 || editTime.length() != 0){
            calendar.setTimeInMillis(task.getDate());
        }

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

                        //Log.d("=== ATDF ", "Year : " + year + " month : " + monthOfYear + " day : " + dayOfMonth);
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

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.setTitle(editTitle.getText().toString());
                task.setDescription(editDescription.getText().toString());
                task.setStatus(ModelTask.STATUS_CURRENT);
                if (editDate.length() != 0 || editTime.length() != 0){
                    task.setDate(calendar.getTimeInMillis());
                    AlarmHelper alarmHelper = AlarmHelper.getInstance();
                    alarmHelper.setAlarm(task);
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
                    calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                    task.setDate(calendar.getTimeInMillis());
                }
                editTaskListener.onTaskEdited(task);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
