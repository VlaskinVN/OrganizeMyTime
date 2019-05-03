package com.example.nicol.organizemytime.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.nicol.organizemytime.MainActivity;
import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.adapter.TaskAdapter;
import com.example.nicol.organizemytime.alarm.AlarmHelper;
import com.example.nicol.organizemytime.dialog.EditTaskDialogFragment;
import com.example.nicol.organizemytime.dialog.ReadTaskDialogFragment;
import com.example.nicol.organizemytime.model.Item;
import com.example.nicol.organizemytime.model.ModelTask;

public abstract class TaskFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected TaskAdapter adapter;
    public MainActivity activity;
    public AlarmHelper alarmHelper;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null){
            activity = (MainActivity) getActivity();
        }

        alarmHelper = AlarmHelper.getInstance();

        addTaskFromDb();
    }

    public abstract void addTask(ModelTask task, boolean saveToDb);

    public void showMenu(View itemView, final int loc){
        PopupMenu popupMenu;
        popupMenu = new PopupMenu(getActivity(), itemView);
        popupMenu.inflate(R.menu.context_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.iReadTask:
                        readTaskDialog(loc);
                        return true;
                    case R.id.iRemoveTask:
                        removeTaskDialog(loc);
                        return true;
                    case R.id.iEditTask:
                        editTaskDialog(loc);
                        return true;
                        default:
                            return false;
                }
            }
        });
        popupMenu.show();
    }

    public void readTaskDialog(int loc){
        Item item = adapter.getItem(loc);
        final ModelTask task = (ModelTask) item;

        if (item.isTask()){
            DialogFragment dialogFragment = new ReadTaskDialogFragment();
            ((ReadTaskDialogFragment) dialogFragment).setTask(task);
            dialogFragment.show(getActivity().getSupportFragmentManager(), "ReadTaskDialogFragment");
        }
    }

    public void editTaskDialog(int loc){
        Item item = adapter.getItem(loc);

        if (item.isTask()){
            final ModelTask task = (ModelTask) item;
            DialogFragment fragment = EditTaskDialogFragment.newInstance(task);
            fragment.show(getActivity().getSupportFragmentManager(), "EditTaskDialogFragment");
        }
    }

    public void updateTask(ModelTask task){
        adapter.updateTask(task);
    }

    public void removeTaskDialog(final int location){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_remove_message);

        Item item = adapter.getItem(location);

        if (item.isTask()){
            final ModelTask task = (ModelTask) item;

            final long timeStamp = task.getTimeStamp();
            final boolean isRemoved[] = {false};

            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.removeItem(location);
                    isRemoved[0] = true;

                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                            "Вы удалили задачу : "+ task.getTitle(), Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addTask(activity.dbHelper.getQueryManager().getTask(timeStamp), false);
                            isRemoved[0] = false;
                        }
                    });

                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            if (isRemoved[0]){
                                alarmHelper.removeAlarm(timeStamp);

                                activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });

                    snackbar.show();

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        builder.show();
    }

    public abstract void addTaskFromDb();

    public abstract void moveTask(ModelTask task);
}
