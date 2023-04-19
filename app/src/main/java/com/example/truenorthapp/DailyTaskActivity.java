package com.example.truenorthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.truenorthapp.Adapter.TaskAdapter;
import com.example.truenorthapp.Model.TaskModel;
import com.example.truenorthapp.Utilities.DatabaseHandling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

// the activity wherein most functionality resides and is implemented. most user interaction will occur here.
public class DailyTaskActivity extends AppCompatActivity implements CloseListener {

    private DatabaseHandling database;
    private RecyclerView taskRecycler;
    private TaskAdapter taskAdapter;
    private FloatingActionButton addButton;
    private FloatingActionButton nightButton;
    private FloatingActionButton dayButton;
    private List<TaskModel> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);
        getSupportActionBar().hide();

        database = new DatabaseHandling(this);
        database.openDatabase();
        taskList = new ArrayList<>();
        addButton = findViewById(R.id.add);
        nightButton = findViewById(R.id.night);
        dayButton = findViewById(R.id.day);

        taskRecycler = findViewById(R.id.taskRecycler);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(database, DailyTaskActivity.this);
        taskRecycler.setAdapter(taskAdapter);

        // implementing the feature that allows us to swipe left and right for different functionalities.
        // refers to the SwipeFunction class and imported ItemTouchHelper for RecyclerViews.
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeFunction(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecycler);
        taskList = database.getTasks();
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);

        // initializing an onclicklistener (click function) for the button to add a new task.
        // allows us to add a new task, referring to the NewTaskFunctions class.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTaskFunctions.newInstance().show(getSupportFragmentManager(),NewTaskFunctions.TAG);
            }
        });

        // initializing an onclicklistener (click function) for the night mode button.
        // flips the colors to night mode.
        nightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

        });

        // initializing an onclicklistener (click function) for the day mode button.
        // allows us to toggle between display modes easily.
        // i would have liked for this floating action button menu to be collapsible.
        // you can't win em all.
        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

    }

    // notifies of dataset changes, and flips the order of the list.
    // we do this to ensure that the first task input stays on top.
    // the autoincrementation of ID by the database would push it down to the bottom.
    @Override
    public void handleDialogClose(DialogInterface d)
    {
        taskList = database.getTasks();
        // reversing the order ensures our first task is at the top of the list
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}