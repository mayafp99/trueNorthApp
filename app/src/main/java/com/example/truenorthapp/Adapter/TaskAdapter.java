package com.example.truenorthapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;
import com.example.truenorthapp.DailyTaskActivity;
import com.example.truenorthapp.Model.TaskModel;
import com.example.truenorthapp.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>
{
    private List<TaskModel> taskList;
    private DailyTaskActivity activity;

    public TaskAdapter(DailyTaskActivity activity)
    {
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder hold, int position)
    {
        TaskModel list = taskList.get(position);
        hold.task.setText(list.getTask());
        hold.task.setChecked(toBoolean(list.getStatus()));
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
