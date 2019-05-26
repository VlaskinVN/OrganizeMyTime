package com.example.nicol.organizemytime.dialog;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.nicol.organizemytime.location.LocationHelper;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.Calendar;

public class AddingTaskDialogFragment extends DialogFragment {

    LocationManager locationManager;
    LocationListener listener;

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
//        final TextInputLayout tilDate = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskDate);
//        final EditText editDate = tilDate.getEditText();
//        TextInputLayout tilTime = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskTime);
//        final EditText editTime = tilTime.getEditText();
        TextInputLayout tilMap = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskMap);
        final EditText editMap = tilMap.getEditText();
        TextInputLayout tilRepeat = (TextInputLayout) containte.findViewById(R.id.tilDialogTaskRepeatDays);
        final EditText editRepeat = tilRepeat.getEditText();


        Spinner spPriority = (Spinner) containte.findViewById(R.id.spDialogTaskPriority);

//        tilDate.setHint(getResources().getString(R.string.task_date));
//        tilTime.setHint(getResources().getString(R.string.task_time));
        tilTitle.setHint(getResources().getString(R.string.task_title));
        tilDecription.setHint(getResources().getString(R.string.task_description));
        tilMap.setHint(getResources().getString(R.string.task_add_map_point));
        tilRepeat.setHint(getResources().getString(R.string.task_repeat_days));

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

        editMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                listener = new LocationListener()  {
                    @Override
                    public void onLocationChanged(Location location) {
                        editMap.setText(location.getLatitude() + "    ---     " + location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                };

                configureButton();
            }
        });

        String days = "";

        editRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment df = RepeatDialogFragment.newInstance();
                ((RepeatDialogFragment) df).setRepeadListeners(new RepeatDialogListener() {
                    @Override
                    public void repeatOptionUserListener(String day, int hour, int minute) {
                        editRepeat.setText(day);
                        task.setRepeatDays(day);

                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        task.setDate(calendar.getTimeInMillis());
                    }

                    @Override
                    public void repeatOneDayListener(Calendar c) {
                        editRepeat.setText(c.getTime().toString());
                        calendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
                        calendar.set(Calendar.MONTH, c.get(Calendar.MONTH));
                        calendar.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
                        calendar.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
                        calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
                        calendar.set(Calendar.SECOND, 0);

                        task.setDate(calendar.getTimeInMillis());

                        task.setRepeatDays("");
                    }

                    @Override
                    public void repeatEverydayListener(boolean every, int hour, int minute) {
                        editRepeat.setText("Ежедневно");
                        task.setRepeatDays("everyday");
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        task.setDate(calendar.getTimeInMillis());
                    }
                });

                df.show(getFragmentManager(), "RepeatDialogFragment");
            }
        });

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.setTitle(editTitle.getText().toString());
                task.setDescription(editDescription.getText().toString());
                task.setStatus(ModelTask.STATUS_CURRENT);
//                if (task.getDate() != 0){
                    AlarmHelper alarmHelper = AlarmHelper.getInstance();
                    if (task.getRepeatDays().length() > 0){
                        if (task.getRepeatDays().equals("everyday")){
                            //Log.d("=== ATDF ", "alarmHelper.setAlarmEveryday(task);");

                            alarmHelper.setAlarmEveryday(task);
                            addingTaskListener.onTaskAdded(task);
                        }else{
                            //Log.d("=== ATDF ", "alarmHelper.setAlarmOptionUser(task);");

                            String repeatDay = task.getRepeatDays();
                            String[] arrday = repeatDay.split(",");
                            repeatDay.trim();

                            String[] days = {"", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};

                            for (int i = 0; i < arrday.length - 1; i++){
                                Log.d("=== AH ", arrday[i].trim());
                                String str = arrday[i].trim();
                                for (int j = 0; j < days.length; j++){
                                    if (str.equals(days[j])){
                                        Log.d("=== AHj ", days[j]);
                                        final Calendar calendars = Calendar.getInstance();

                                        switch (j){
                                            case 1:
                                                calendars.set(Calendar.DAY_OF_WEEK, 1);
                                                task.setDate(calendars.getTimeInMillis());
                                                addingTaskListener.onTaskAdded(task);alarmHelper.setAlarm(task);
                                                Log.d("=== AH1 ", days[j] + " --- " + calendars.get(Calendar.DATE) + " " + calendars.get(Calendar.MONTH));
//                                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                                break;
                                            case 2:
                                                calendars.set(Calendar.DAY_OF_WEEK, 2);
                                                task.setDate(calendars.getTimeInMillis());
                                                addingTaskListener.onTaskAdded(task);alarmHelper.setAlarm(task);
                                                Log.d("=== AH2 ", days[j] + " --- " + calendars.get(Calendar.DATE) + " " + calendars.get(Calendar.MONTH));
//                                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                                break;
                                            case 3:
                                                calendars.set(Calendar.DAY_OF_WEEK, 3);
                                                task.setDate(calendars.getTimeInMillis());
                                                addingTaskListener.onTaskAdded(task);alarmHelper.setAlarm(task);
                                                Log.d("=== AH3 ", days[j] + " --- " + calendars.get(Calendar.DATE) + " " + calendars.get(Calendar.MONTH));
//                                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                                break;
                                            case 4:
                                                calendars.set(Calendar.DAY_OF_WEEK, 4);
                                                task.setDate(calendars.getTimeInMillis());
                                                addingTaskListener.onTaskAdded(task);alarmHelper.setAlarm(task);
                                                Log.d("=== AH4 ", days[j] + " --- " + calendars.get(Calendar.DATE) + " " + calendars.get(Calendar.MONTH));
//                                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                                break;
                                            case 5:
                                                calendars.set(Calendar.DAY_OF_WEEK, 5);
                                                task.setDate(calendars.getTimeInMillis());
                                                addingTaskListener.onTaskAdded(task);alarmHelper.setAlarm(task);
                                                Log.d("=== AH5 ", days[j] + " --- " + calendars.get(Calendar.DATE) + " " + calendars.get(Calendar.MONTH));
//                                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                                break;
                                            case 6:
                                                calendars.set(Calendar.DAY_OF_WEEK, 6);
                                                task.setDate(calendars.getTimeInMillis());
                                                addingTaskListener.onTaskAdded(task);alarmHelper.setAlarm(task);
                                                Log.d("=== AH6 ", days[j] + " --- " + calendars.get(Calendar.DATE) + " " + calendars.get(Calendar.MONTH));
//                                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                                break;
                                            case 7:
                                                calendars.set(Calendar.DAY_OF_WEEK, 7);
                                                task.setDate(calendars.getTimeInMillis());
                                                addingTaskListener.onTaskAdded(task);alarmHelper.setAlarm(task);
                                                Log.d("=== AH7 ", days[j] + " --- " + calendars.get(Calendar.DATE) + " " + calendars.get(Calendar.MONTH));
//                                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                                break;
                                        }
                                    }

                                }
                            }

                            //alarmHelper.setAlarmOptionUser(task);
                        }
                    }else{
                        task.setDate(calendar.getTimeInMillis());
                        //Log.d("=== ATDF ", "alarmHelper.setAlarm(task);");
                        alarmHelper.setAlarm(task);
                        addingTaskListener.onTaskAdded(task);
                    }
//
//                } else {
//                    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
//                    calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
//                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
//                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
//                    task.setDate(calendar.getTimeInMillis());
//
//                    AlarmHelper alarmHelper = AlarmHelper.getInstance();
//                    alarmHelper.setAlarm(task);
//                }

                //addingTaskListener.onTaskAdded(task);
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

//                editDate.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR) ||
//                                calendar.get(Calendar.MONTH) < Calendar.getInstance().get(Calendar.MONTH) ||
//                                calendar.get(Calendar.YEAR) < Calendar.getInstance().get(Calendar.YEAR)){
//                            positiveButton.setEnabled(false);
//                            tilDate.setError("Выбранна некоректная дата!");
//                        }else{
//                            positiveButton.setEnabled(true);
//                            tilDate.setErrorEnabled(false);
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });

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

    private void configureButton() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                return;
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }
}
