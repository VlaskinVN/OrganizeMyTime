package com.example.nicol.organizemytime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.adapter.CurrentTasksAdapter;
import com.example.nicol.organizemytime.database.DbHelper;
import com.example.nicol.organizemytime.model.ModelSeparator;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CurrentTaskFragment extends TaskFragment {

    public interface OnTaskDoneListener{
        void onTaskDone(ModelTask task);
    }

    OnTaskDoneListener onTa;

    public CurrentTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onTa = (OnTaskDoneListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + " must implements OnTaskDoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CurrentTasksAdapter(this, getActivity());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void addTask(ModelTask task, boolean saveToDb) {
        int position = -1;

        ModelSeparator separator = null;

        for (int i = 0; i < adapter.getItemCount(); i++){
            if (adapter.getItem(i).isTask()){
                ModelTask t = (ModelTask) adapter.getItem(i);
                if (task.getDate() < t.getDate()){
                    position = i;
                    break;
                }
            }
        }

        if (task.getDate() != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(task.getDate());

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)){
                task.setDateStatus(ModelSeparator.SEP_OVERDUE);
                if (!adapter.sepOverdue){
                    adapter.sepOverdue = true;
                    separator = new ModelSeparator(ModelSeparator.SEP_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)){
                task.setDateStatus(ModelSeparator.SEP_TODAY);
                if (!adapter.sepToday){
                    adapter.sepToday = true;
                    separator = new ModelSeparator(ModelSeparator.SEP_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1){
                task.setDateStatus(ModelSeparator.SEP_TOMORROW);
                if (!adapter.sepTomorow){
                    adapter.sepTomorow = true;
                    separator = new ModelSeparator(ModelSeparator.SEP_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1){
                task.setDateStatus(ModelSeparator.SEP_FUTURE);
                if (!adapter.sepFuture){
                    adapter.sepFuture = true;
                    separator = new ModelSeparator(ModelSeparator.SEP_FUTURE);
                }
            }
        }

        if (position != -1){
            if (!adapter.getItem(position - 1).isTask()){
                if (position - 2 >= 0 && adapter.getItem(position - 2).isTask()){
                    ModelTask modelTask = (ModelTask) adapter.getItem(position - 2);
                    if (task.getDateStatus() == modelTask.getDateStatus()){
                        position -= 1;
                    }
                } else if (position - 2 < 0 && task.getDate() == 0){
                    position -= 1;
                }
            }

            if (separator != null){
                adapter.addItem(position, task);
            }

            adapter.addItem(position, task);
        } else {
            if (separator != null){
                adapter.addItem(separator);
            }
            adapter.addItem(task);
        }

        if (saveToDb){
            activity.dbHelper.saveTask(task);
        }
    }

    @Override
    public void addTaskFromDb() {
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.getQueryManager().getTasks(DbHelper.DB_SELECTION_STATUS
                        + " OR "
                        + DbHelper.DB_SELECTION_STATUS,
                new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                        Integer.toString(ModelTask.STATUS_OVERDUE)},
                DbHelper.DB_TASK_DATE
        ));

        for(int i = 0; i < tasks.size(); i++){
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void moveTask(ModelTask task) {
        alarmHelper.removeAlarm(task.getTimeStamp());
        onTa.onTaskDone(task);
        //Log.d("=== CTF ", "moveTask - task.getTitle() : " + task.getTitle());
    }
}
