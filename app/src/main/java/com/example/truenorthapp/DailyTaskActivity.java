package com.example.truenorthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.truenorthapp.Adapter.TaskAdapter;
import com.example.truenorthapp.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DailyTaskActivity extends AppCompatActivity {

    private RecyclerView taskRecycler;
    private TaskAdapter taskAdapter;
    private List<TaskModel> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);
        getSupportActionBar().hide();

        taskList = new ArrayList<>();

        taskRecycler = findViewById(R.id.taskRecycler);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this);
        taskRecycler.setAdapter(taskAdapter);

        // dummy data

        TaskModel task = new TaskModel();
        task.setTask("test task!");
        // since the boolean only accepts 0 or 1,
        // in which 0 = false, 1 = true, we pass those values to test.
        task.setStatus(0);
        task.setId(1);
        // append to the list to display as many times as we want.
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);

        taskAdapter.setTask(taskList);


    }
}