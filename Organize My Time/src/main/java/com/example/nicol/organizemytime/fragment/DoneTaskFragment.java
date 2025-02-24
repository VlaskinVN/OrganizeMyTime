package com.example.nicol.organizemytime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.adapter.CurrentTasksAdapter;
import com.example.nicol.organizemytime.adapter.DoneTaskAdapter;
import com.example.nicol.organizemytime.database.DbHelper;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

public class DoneTaskFragment extends TaskFragment {

    public DoneTaskFragment() {
        // Required empty public constructor
    }

    public interface OnTaskRestoreListener{
        void onTaskRestore(ModelTask task);
    }

    public OnTaskRestoreListener onTaskRestoreListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onTaskRestoreListener = (OnTaskRestoreListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + " must implements OnTaskRestoreListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDoneTasks);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoneTaskAdapter(this);
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void addTaskFromDb() {
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.getQueryManager().getTasks(
                DbHelper.DB_SELECTION_STATUS,
                new String[]{Integer.toString(ModelTask.STATUS_DONE)},
                DbHelper.DB_TASK_DATE
                ));

        for (int i = 0; i < tasks.size(); i++){
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void moveTask(ModelTask task) {
        if (task.getDate() != 0){
            alarmHelper.setAlarm(task);
        }
        onTaskRestoreListener.onTaskRestore(task);
    }

    @Override
    public void addTask(ModelTask task, boolean saveToDb) {
        int position = -1;

        for (int i = 0; i < adapter.getItemCount(); i++){
            if (adapter.getItem(i).isTask()){
                ModelTask t = (ModelTask) adapter.getItem(i);
                if (task.getDate() < t.getDate()){
                    position = i;
                    break;
                }
            }
        }

        if (position != -1){
            adapter.addItem(position, task);
        } else {
            adapter.addItem(task);
        }

        if (saveToDb){
            activity.dbHelper.saveTask(task);
        }
    }
}
