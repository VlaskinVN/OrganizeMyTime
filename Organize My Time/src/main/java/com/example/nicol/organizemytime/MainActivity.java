package com.example.nicol.organizemytime;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.nicol.organizemytime.adapter.TabAdapter;
import com.example.nicol.organizemytime.database.DbHelper;
import com.example.nicol.organizemytime.dialog.AddingTaskDialogFragment;
import com.example.nicol.organizemytime.fragment.CurrentTaskFragment;
import com.example.nicol.organizemytime.fragment.DoneTaskFragment;
import com.example.nicol.organizemytime.model.ModelTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddingTaskDialogFragment.AddingTaskListener, CurrentTaskFragment.OnTaskDoneListener, DoneTaskFragment.OnTaskRestoreListener {

    private FragmentManager fragmentMennager;
    private TabAdapter tabAdapter;
    private CurrentTaskFragment currentTaskFragment;
    private DoneTaskFragment doneTaskFragment;
    public DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fragmentMennager = getSupportFragmentManager();
        dbHelper = new DbHelper(getApplicationContext());

        setUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                break;
        }

        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
            setSupportActionBar(toolbar);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = findViewById(R.id.pager);
        tabAdapter = new TabAdapter(fragmentMennager, 2);

        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        currentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        doneTaskFragment = (DoneTaskFragment) tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(fragmentMennager, "AddingTaskDialogFragment");
            }
        });
    }

    @Override
    public void onTaskAdded(ModelTask newTask) {
        Toast.makeText(this, "Task added", Toast.LENGTH_LONG).show();
        currentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, "canceled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskDone(ModelTask task) {
        doneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        currentTaskFragment.addTask(task, false);
    }
}
