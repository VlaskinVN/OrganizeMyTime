package com.example.nicol.organizemytime.adapter;

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

import java.util.ArrayList;
import java.util.List;

public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Item> items;
    TaskFragment taskFragment;

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

    public void removeItem(int position){
        if (position >= 0 && position <= getItemCount() -1){
            items.remove(position);
            notifyItemRemoved(position);
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
    public TaskFragment getTaskFragment(){
        return taskFragment;
    }
}
