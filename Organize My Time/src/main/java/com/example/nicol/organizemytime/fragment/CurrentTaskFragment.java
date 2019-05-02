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
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.ArrayList;
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
        onTa.onTaskDone(task);
        //Log.d("=== CTF ", "moveTask - task.getTitle() : " + task.getTitle());
    }
}
