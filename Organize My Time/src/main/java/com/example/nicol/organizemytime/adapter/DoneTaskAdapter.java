package com.example.nicol.organizemytime.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.Utils;
import com.example.nicol.organizemytime.fragment.TaskFragment;
import com.example.nicol.organizemytime.model.Item;
import com.example.nicol.organizemytime.model.ModelTask;

public class DoneTaskAdapter extends TaskAdapter {
    public DoneTaskAdapter(TaskFragment taskFragment) {
        super(taskFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_task, viewGroup, false);
        TextView title = (TextView) v.findViewById(R.id.tvTaskTitle);
        TextView date = (TextView) v.findViewById(R.id.tvTaskDate);
        View priority = (View) v.findViewById(R.id.vPriority);
        TextView decription = (TextView) v.findViewById(R.id.tvTaskDescription);

        return new TaskViewHolder(v, title, date, priority, decription);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Item item = items.get(i);

        if (item.isTask()){
            viewHolder.itemView.setEnabled(true);

            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;

            final View view = taskViewHolder.itemView;
            final Resources resources = view.getResources();

            taskViewHolder.title.setText(task.getTitle());
            taskViewHolder.decription.setText(task.getDescription());
            if (task.getDate() != 0){
                taskViewHolder.date.setText(Utils.getFullDate(task.getDate()));
            } else {
                taskViewHolder.date.setText(null);
            }

            view.setVisibility(View.VISIBLE);
            taskViewHolder.priority.setEnabled(true);

            view.setBackground(resources.getDrawable(R.drawable.status_rect));
            taskViewHolder.title.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            taskViewHolder.date.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            taskViewHolder.priority.setBackground(resources.getDrawable(task.getPriorityColor()));

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    getTaskFragment().showMenu(view, taskViewHolder.getLayoutPosition());
                    return true;
                }
            });

            taskViewHolder.priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskViewHolder.priority.setEnabled(false);
                    task.setStatus(ModelTask.STATUS_DONE);

                    getTaskFragment().activity.dbHelper.getUpdateManager().statusUpdate(task.getTimeStamp(), ModelTask.STATUS_CURRENT);

                    view.setBackground(resources.getDrawable(R.drawable.status_rect_selected));
                    taskViewHolder.title.setTextColor(resources.getColor(R.color.colorPrimaryDark));
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.colorPrimaryDark));
                    taskViewHolder.priority.setBackground(resources.getDrawable(task.getPriorityColor()));


                    if (task.getStatus() == ModelTask.STATUS_DONE){
                        ObjectAnimator tx = ObjectAnimator.ofFloat(view, "translationX", 0f, view.getWidth());
                        ObjectAnimator txBack = ObjectAnimator.ofFloat(view, "translationX", view.getWidth(), 0f);

                        tx.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                view.setVisibility(View.GONE);
                                getTaskFragment().moveTask(task);
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

        }

    }
}
