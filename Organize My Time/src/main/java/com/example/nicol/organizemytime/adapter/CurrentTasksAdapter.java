package com.example.nicol.organizemytime.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.media.midi.MidiOutputPort;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.Utils;
import com.example.nicol.organizemytime.dialog.ReadTaskDialogFragment;
import com.example.nicol.organizemytime.fragment.CurrentTaskFragment;
import com.example.nicol.organizemytime.model.Item;
import com.example.nicol.organizemytime.model.ModelSeparator;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.Calendar;


public class CurrentTasksAdapter extends TaskAdapter {
    private static final int TYPE_TASK = 0;
    private static final int TYPE_SEPARATOR = 1;
    private Context context;

    public CurrentTasksAdapter(CurrentTaskFragment taskFragment, Context context) {
        super(taskFragment);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i){
            case TYPE_TASK:
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_task, viewGroup, false);
                TextView title = (TextView) v.findViewById(R.id.tvTaskTitle);
                TextView date = (TextView) v.findViewById(R.id.tvTaskDate);
                View priority = (View) v.findViewById(R.id.vPriority);
                TextView decription = (TextView) v.findViewById(R.id.tvTaskDescription);

                viewGroup.getContext();

                return new TaskViewHolder(v, title, date, priority, decription);
            case TYPE_SEPARATOR:
                View sep = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_separator, viewGroup, false);
                TextView type = (TextView) sep.findViewById(R.id.tvSepName);
                return new SeparatorViewHolder(sep, type);
                default:
                    return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Item item = items.get(i);


        final Resources resources = viewHolder.itemView.getResources();

        if (item.isTask()){
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;

            final View itemView = taskViewHolder.itemView;

            taskViewHolder.title.setText(task.getTitle());
            taskViewHolder.decription.setText(task.getDescription());
            if (task.getDate() != 0){
                taskViewHolder.date.setText(Utils.getFullDate(task.getDate()));
            } else {
                taskViewHolder.date.setText(null);
            }

            itemView.setVisibility(View.VISIBLE);
            taskViewHolder.priority.setEnabled(true);

            /*if (task.getDate() != 0 && task.getDate() < Calendar.getInstance().getTimeInMillis()){
                itemView.setBackgroundColor(resources.getColor(R.color.gray_200));
            }else{
                itemView.setBackgroundColor(resources.getColor(R.color.gray_50));
            }*/

            itemView.setBackground(resources.getDrawable(R.drawable.status_rect));
            taskViewHolder.title.setTextColor(resources.getColor(R.color.darkblue));
            taskViewHolder.date.setTextColor(resources.getColor(R.color.priorityLowSelected));
            taskViewHolder.priority.setBackground(resources.getDrawable(task.getPriorityColor()));

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    getTaskFragment().showMenu(itemView, taskViewHolder.getLayoutPosition());
                    return true;
                }
            });

            taskViewHolder.priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskViewHolder.priority.setEnabled(false);
                    task.setStatus(ModelTask.STATUS_DONE);

                    getTaskFragment().activity.dbHelper.getUpdateManager().statusUpdate(task.getTimeStamp(), ModelTask.STATUS_DONE);

                    itemView.setBackground(resources.getDrawable(R.drawable.status_rect));
                    taskViewHolder.title.setTextColor(resources.getColor(R.color.darkblue));
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.priorityLowSelected));
                    taskViewHolder.priority.setBackground(resources.getDrawable(task.getPriorityColor()));


                    if (task.getStatus() == ModelTask.STATUS_DONE){
                        ObjectAnimator tx = ObjectAnimator.ofFloat(itemView, "translationX", 0f, itemView.getWidth());
                        ObjectAnimator txBack = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getWidth(), 0f);

                        tx.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                itemView.setVisibility(View.GONE);
                                getTaskFragment().moveTask(task);
                                //Log.d("=== CTA ", "onAnimationEnd - getTaskFragment().moveTask(task) : " + task.getTitle());
                                removeItem(taskViewHolder.getLayoutPosition());
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                        AnimatorSet ts = new AnimatorSet();
                        ts.play(tx).before(txBack);
                        ts.start();
                    }
                }
            });
        } else {
            ModelSeparator sep = (ModelSeparator) item;
            SeparatorViewHolder separatorViewHolder = (SeparatorViewHolder) viewHolder;
            separatorViewHolder.type.setText(resources.getString(sep.getType()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isTask()){
            return TYPE_TASK;
        }else {
            return TYPE_SEPARATOR;
        }
    }
}
