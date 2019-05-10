package com.example.nicol.organizemytime.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nicol.organizemytime.R;
import com.example.nicol.organizemytime.ReadDialogActivity;
import com.example.nicol.organizemytime.Utils;
import com.example.nicol.organizemytime.model.ModelTask;

public class ReadTaskDialogFragment extends DialogFragment {

    private ReadTaskDialogListener rtdl;

    public interface ReadTaskDialogListener{
        public void onCloseDialog();
    }

    private ModelTask task;

    public ModelTask getTask() {
        return task;
    }

    public void setTask(ModelTask task) {
        this.task = task;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.rtdl = (ReadTaskDialogListener) getActivity();
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Просмотр задачи : " + task.getTitle());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View container = inflater.inflate(R.layout.dialog_task_read, null);

        final TextInputLayout tilTitle = container.findViewById(R.id.tilDialogTaskReadTitle);
        final EditText tvTitle = tilTitle.getEditText();
        final TextInputLayout tilDate = container.findViewById(R.id.tilDialogTaskReadDate);
        final EditText tvDate = tilDate.getEditText();
        final TextInputLayout tilDesc = container.findViewById(R.id.tilDialogTaskReadDescription);
        final EditText tvDesc = tilDesc.getEditText();
        final TextInputLayout tilPriority = container.findViewById(R.id.tilDialogTaskReadPriority);
        final EditText tvPriority = tilPriority.getEditText();
        final TextInputLayout tilStatus = container.findViewById(R.id.tilDialogTaskReadStatus);
        final EditText tvStatus = tilStatus.getEditText();

        tilTitle.setHint(getResources().getString(R.string.task_title));
        tilDate.setHint(getResources().getString(R.string.task_date));
        tilDesc.setHint(getResources().getString(R.string.task_description));
        tilPriority.setHint(getResources().getString(R.string.task_priority));
        tilStatus.setHint(getResources().getString(R.string.task_status));
        //Log.d("=== RTDF ", "task.getPriority() : " + task.getPriority());

        tvTitle.setText(task.getTitle());
        tvDate.setText(Utils.getFullDate(task.getDate()));
        tvDesc.setText(task.getDescription());
        if (task.getPriority() == -1){
            tvPriority.setText(Utils.getPriorityType(ModelTask.PRIORITY_LOW));
        }else{
            tvPriority.setText(Utils.getPriorityType(task.getPriority()));
        }
        if (task.getStatus() == -1){
            tvStatus.setText(Utils.getStatusType(ModelTask.STATUS_CURRENT));
        }else{
            tvStatus.setText(Utils.getStatusType(task.getStatus()));
        }


        dialog.setView(container);

        dialog.setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (getActivity().getLocalClassName().equals(ReadDialogActivity.class.getSimpleName())){
                    rtdl.onCloseDialog();
                }
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = dialog.create();

        return alertDialog;
    }
}
