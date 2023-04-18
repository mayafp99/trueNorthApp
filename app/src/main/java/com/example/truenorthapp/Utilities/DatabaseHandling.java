package com.example.truenorthapp.Utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.truenorthapp.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandling extends SQLiteOpenHelper {
    // defining the version of our database
    private static final int VERSION = 1;
    // defining our database name
    private static final String NAME = "taskDatabase";
    // defining our TABLE name
    private static final String TASK_TABLE = "tasks";
    // defining column names/IDs
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    // defining the query used to create the database. entire query defined as a string...
    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  "
                                                    + TASK + " TEXT, " + STATUS + " INTEGER)";
    // defining variables
    private SQLiteDatabase database;

    // constructors for a database helper/handler class
    public DatabaseHandling(Context c){
        super(c, NAME, null, VERSION);
    }

    // write oncreate method
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        // query to create a new table so that we have a table upon the database opening
        database.execSQL(CREATE_TASK_TABLE);
    }

    // write onupgrade method to allow us to drop the task table on upgrade,
    // so we have a new one
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer)
    {
        // drop existing tables
        database.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);

        // create new/blank table
        onCreate(database);
    }

    // open database method to allow us to work with the database itself
    public void openDatabase()
    {
        database = this.getWritableDatabase();
    }

    // insert task functions
    // we do not need to initialize ID as autoincrementation already occurs
    public void insertTask(TaskModel taskModel)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, taskModel.getTask());
        // hardcoding status as 0 at this point
        // because any new task will be automatically unchecked.
        contentValues.put(STATUS, 0);
        database.insert(TASK_TABLE, null, contentValues);
    }

    // retrieving all tasks in database and storing in array list.
    @SuppressLint("Range")
    public List<TaskModel> getTasks()
    {
        List<TaskModel> taskList = new ArrayList<>();
        Cursor c = null;
        // transaction statement to avoid errors with larger quantities of data.
        // allows us to safely store data.
        database.beginTransaction();
        try{
            c = database.query(TASK_TABLE, null, null, null, null, null, null, null);
            if(c != null){
                if(c.moveToFirst()){
                    do{
                        TaskModel task = new TaskModel();
                        task.setId(c.getInt(c.getColumnIndex(ID)));
                        task.setTask(c.getString(c.getColumnIndex(TASK)));
                        task.setStatus(c.getInt(c.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(c.moveToNext());
                }
            }
        }
        finally {
            database.endTransaction();
            c.close();
        }
        return taskList;
    }

    // marking task completion
    public void statusUpdate(int id, int status)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        database.update(TASK_TABLE, contentValues, ID + "=?", new String[] {String.valueOf(id)});
    }

    // updating tasks
    public void taskUpdate(int id, String task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task);
        database.update(TASK_TABLE, contentValues, ID + "=?", new String[] {String.valueOf(id)});
    }

    // to delete a stored task
    public void taskDelete(int id)
    {
        database.delete(TASK_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}

