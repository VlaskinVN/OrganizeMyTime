package com.example.nicol.organizemytime;

import android.opengl.Visibility;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.nicol.organizemytime.dialog.ReadTaskDialogFragment;
import com.example.nicol.organizemytime.model.ModelTask;

public class ReadDialogActivity extends AppCompatActivity implements ReadTaskDialogFragment.ReadTaskDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_read_dialog);

        Bundle bundle = getIntent().getExtras();
        ModelTask task = bundle.getParcelable("data");

        DialogFragment dialogFragment = new ReadTaskDialogFragment();
        ((ReadTaskDialogFragment) dialogFragment).setTask(task);
        dialogFragment.show(getSupportFragmentManager(), "ReadTaskDialogFragment");
    }

    @Override
    public void onCloseDialog() {
        finish();
    }
}
