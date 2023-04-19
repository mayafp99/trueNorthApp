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

// functionality of the page where we add a new task to our to do list.
// works with our new_task.xml
public class NewTaskFunctions extends BottomSheetDialogFragment
{
    // identifying a tag that uniquely identifies any dialog fragment in our app
    // as well as some other variables we will use, including the instance of our database.
    // also initializing allowance to edit text, save the task, and instantiating the database.
    public static final String TAG = "BottomDialog";
    private EditText newTask;
    private Button saveButton;
    private DatabaseHandling database;

    public static NewTaskFunctions newInstance()
    {
           return new NewTaskFunctions();
    }
    // sets the visual elements of our dialogue in coordinance with what is set in the themes.xml file.
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    // dictates what we see on view creation.
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup cont, Bundle savedState)
    {
        View v = inf.inflate(R.layout.new_task, cont, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return v;
    }

    // we begin handling the database. We call both the area for the user to type in a new task,
    // and assign the button to save it functionality within this code.
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
        // passing our data to bottom sheet dialog fragment.
        // also making it look cohesive with the rest of the app, as much as possible.
        if(b != null)
        {
            updated = true;
            String task = b.getString("task");
            newTask.setText(task);

            if(task.length() > 0)
                saveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.task_background));
        }

        // instantiating a response to when text is changed within the task prompt.
        newTask.addTextChangedListener(new TextWatcher() {
            // we will not be using before or after text changed but
            // program throws an error if we do not have it.
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            // so long as there is no change/no new task, the save button is not enabled.
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.text));
                }
                else
                {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.task_text));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        boolean finalUpdated = updated;

        // onclicklistener for the save button, giving it functionality.
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            // updates the task ID and commits it to the database. the numeric boolean implies whether the task has been updated (1) or not (0).
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
    // update recycler view, which the user sees.
    @Override
    public void onDismiss(DialogInterface d)
    {
        Activity a = getActivity();
        // CloseListener is a simple function that will actually execute all database and recyclerview tasks.
        if(a instanceof CloseListener)
        {
            ((CloseListener)a).handleDialogClose(d);
        }
    }

}
