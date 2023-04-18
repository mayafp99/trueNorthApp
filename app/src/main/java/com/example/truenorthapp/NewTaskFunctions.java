package com.example.truenorthapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.truenorthapp.Model.TaskModel;
import com.example.truenorthapp.Utilities.DatabaseHandling;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import javax.annotation.Nullable;

public class NewTaskFunctions extends BottomSheetDialogFragment
{
    // identifying a tag that uniquely identifies any dialog fragment in our app
    // as well as some other variables we will use, including the instance of our database.
    public static final String TAG = "BottomDialog";
    private EditText newTask;
    private Button saveButton;
    private DatabaseHandling database;

    public static NewTaskFunctions newInstance()
    {
           return new NewTaskFunctions();
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup cont, Bundle savedState)
    {
        View v = inf.inflate(R.layout.new_task, cont, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return v;
    }

    @Nullable
    @Override
    public void onViewCreated(View v, Bundle savedState)
    {
        super.onViewCreated(v, savedState);
        newTask = getView().findViewById(R.id.newTaskInput);
        saveButton = getView().findViewById(R.id.newTaskButton);

        database = new DatabaseHandling(getActivity());
        database.openDatabase();

        // checking if we are updating or creating a new task
        // this allows the database to execute the appropriate function
        boolean updated = false;
        final Bundle b = getArguments();
        // passing our data to bottom sheet dialog frag
        if(b != null)
        {
            updated = true;
            String task = b.getString("task");
            newTask.setText(task);

            if(task.length() > 0)
                saveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.ash_dust));
        }
        newTask.addTextChangedListener(new TextWatcher() {
            // we will not be using before or after text changed but
            // program throws an error if we do not have it.
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.add_background));
                }
                else
                {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.ash_dust));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalUpdated = updated;
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String t = newTask.getText().toString();
                if(finalUpdated)
                {
                    database.taskUpdate(b.getInt("id"), t);
                }
                else
                {
                    TaskModel task = new TaskModel();
                    task.setTask(t);
                    task.setStatus(0);
                    database.insertTask(task);
                }

                dismiss();
            }
        });
    }
    // update recycler view
    @Override
    public void onDismiss(DialogInterface d)
    {
        Activity a = getActivity();
        // CloseListener is a function that will actually execute all database and recyclerview tasks.
        if(a instanceof CloseListener)
        {
            ((CloseListener)a).handleDialogClose(d);
        }
    }

}
