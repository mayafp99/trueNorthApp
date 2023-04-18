package com.example.truenorthapp;

import androidx.appcompat.app.AppCompatActivity;
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

public class DailyTaskActivity extends AppCompatActivity implements CloseListener {

    private DatabaseHandling database;
    private RecyclerView taskRecycler;
    private TaskAdapter taskAdapter;
    private FloatingActionButton addButton;
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

        taskRecycler = findViewById(R.id.taskRecycler);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(database, DailyTaskActivity.this);
        taskRecycler.setAdapter(taskAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeFunction(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecycler);
        taskList = database.getTasks();
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTaskFunctions.newInstance().show(getSupportFragmentManager(),NewTaskFunctions.TAG);
            }
        });

    }

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