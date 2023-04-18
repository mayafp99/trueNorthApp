package com.example.truenorthapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;
import com.example.truenorthapp.DailyTaskActivity;
import com.example.truenorthapp.Model.TaskModel;
import com.example.truenorthapp.NewTaskFunctions;
import com.example.truenorthapp.R;
import com.example.truenorthapp.Utilities.DatabaseHandling;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>
{
    private List<TaskModel> taskList;
    private DailyTaskActivity activity;
    private DatabaseHandling database;

    public TaskAdapter(DatabaseHandling database, DailyTaskActivity activity)
    {
        this.database = database;
        this.activity = activity;
    }

    public Context getContext()
    {
        return activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder hold, int position)
    {
        database.openDatabase();
        TaskModel list = taskList.get(position);
        hold.task.setText(list.getTask());
        hold.task.setChecked(toBoolean(list.getStatus()));
        hold.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    database.statusUpdate(list.getId(), 1);
                }
                else{
                    database.statusUpdate(list.getId(), 0);
                }
            }
        });
    }

    public int getItemCount()
    {
        return taskList.size();
    }

    private boolean toBoolean(int n)
    {
        return n != 0;
    }

    public void setTask(List<TaskModel> taskList)
    {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void setTasksList(List<TaskModel> taskList)
    {
        this.taskList = taskList;
    }

    public void deleteItem(int position)
    {
        TaskModel item = taskList.get(position);
        database.taskDelete(item.getId());
        taskList.remove(position);
        notifyItemRemoved(position);
    }


    public void editTask(int position)
    {
        TaskModel item = taskList.get(position);
        Bundle b = new Bundle();
        b.putInt("id",item.getId());
        b.putString("task", item.getTask());
        NewTaskFunctions frag = new NewTaskFunctions();
        frag.setArguments(b);
        frag.show(activity.getSupportFragmentManager(), NewTaskFunctions.TAG);
    }


    public static class ViewHolder extends  RecyclerView.ViewHolder
    {
        CheckBox task;
        ViewHolder(View v)
        {
            super(v);
            task = v.findViewById(R.id.listBox);
        }
    }
}
