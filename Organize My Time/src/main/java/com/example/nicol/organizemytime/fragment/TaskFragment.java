package com.example.nicol.organizemytime.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.nicol.organizemytime.adapter.CurrentTasksAdapter;
import com.example.nicol.organizemytime.adapter.TaskAdapter;
import com.example.nicol.organizemytime.model.ModelTask;

public abstract class TaskFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected TaskAdapter adapter;

    public void addTask(ModelTask task){
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
    }

    public abstract void moveTask(ModelTask task);
}
