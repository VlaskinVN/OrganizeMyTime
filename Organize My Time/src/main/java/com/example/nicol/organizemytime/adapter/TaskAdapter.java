package com.example.nicol.organizemytime.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.fragment.TaskFragment;
import com.example.nicol.organizemytime.model.Item;
import com.example.nicol.organizemytime.model.ModelSeparator;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Item> items;
    TaskFragment taskFragment;
    public boolean sepOverdue;
    public boolean sepToday;
    public boolean sepTomorow;
    public boolean sepFuture;

    public TaskAdapter(TaskFragment taskFragment) {
        this.taskFragment = taskFragment;
        this.items = new ArrayList<>();
    }

    public Item getItem(int pos) {
        return items.get(pos);
    }

    public void addItem(Item item){
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int location, Item item){
        items.add(location, item);
        notifyItemInserted(location);
    }

    public void updateTask(ModelTask task){
        for (int i = 0; i < getItemCount(); i++){
            if (getItem(i).isTask()){
                ModelTask t = (ModelTask) getItem(i);
                if (task.getTimeStamp() == t.getTimeStamp()){
                    removeItem(i);
                    getTaskFragment().addTask(task, false);
                }
            }
        }
    }

    public void removeItem(int position){
        if (position >= 0 && position <= getItemCount() -1){
            items.remove(position);
            notifyItemRemoved(position);
            if (position -1 >= 0 && position <= getItemCount() -1){
                if (!getItem(position).isTask() && !getItem(position -1).isTask()){
                    ModelSeparator separator = (ModelSeparator) getItem(position - 1);
                    checSep(separator.getType());
                    items.remove(position - 1);
                    notifyItemRemoved(position - 1);
                }
            } else if (getItemCount() - 1 >= 0 && !getItem((getItemCount() - 1)).isTask()){
                ModelSeparator separator = (ModelSeparator) getItem(getItemCount() - 1);
                checSep(separator.getType());

                int loc = getItemCount() - 1;
                items.remove(loc);
                notifyItemRemoved(loc);
            }
        }
    }

    public void checSep(int type){
        switch (type){
            case ModelSeparator.SEP_OVERDUE:
                sepOverdue = false;
                break;
            case ModelSeparator.SEP_TODAY:
                sepToday = false;
                break;
            case ModelSeparator.SEP_TOMORROW:
                sepTomorow = false;
                break;
            case ModelSeparator.SEP_FUTURE:
                sepFuture = false;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class TaskViewHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected TextView date;
        protected View priority;
        protected TextView decription;

        public TaskViewHolder(View itemView, TextView title, TextView date, View priority, TextView decription) {
            super(itemView);
            this.title = title;
            this.date = date;
            this.priority = priority;
            this.decription = decription;
        }
    }

    protected class SeparatorViewHolder extends RecyclerView.ViewHolder{

        protected TextView type;

        public SeparatorViewHolder(View itemView, TextView type) {
            super(itemView);
            this.type = type;
        }
    }

    public TaskFragment getTaskFragment(){
        return taskFragment;
    }
}
