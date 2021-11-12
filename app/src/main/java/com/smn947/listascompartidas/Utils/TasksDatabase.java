package com.smn947.listascompartidas.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.util.Log;

import com.smn947.listascompartidas.Models.ModelLists;
import com.smn947.listascompartidas.Models.ModelTasks;

import java.util.ArrayList;
import java.util.List;


public class TasksDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String NAME = "SMN947_ListasCompartidas";
    private static final String TASKS_TABLE = "Tareas";
    private static final String tb_ID = "id";
    private static final String tb_LIST = "lista";
    private static final String tb_NAME = "tarea";
    private static final String tb_STATUS = "status";
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TASKS_TABLE + "(" + tb_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tb_LIST + " INTEGER," + tb_NAME + " TEXT)";

    private SQLiteDatabase db;

    public TasksDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ModelTasks task){
        ContentValues cv = new ContentValues();
        Log.d("DB Insert", task.getList() + " | " + task.getTask());
        cv.put(tb_NAME, task.getTask());
        cv.put(tb_LIST, task.getList());
        db.insert(TASKS_TABLE, null, cv);
    }

    public List<ModelTasks> getAllTasks(String list){
        List<ModelTasks> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            String whereClause = "lista=?";
            String [] whereArgs = {list};
            cur = db.query(TASKS_TABLE, null, whereClause, whereArgs, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ModelTasks task = new ModelTasks();
                        task.setId(cur.getInt(cur.getColumnIndex(tb_ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(tb_NAME)));
                        task.setList(cur.getString(cur.getColumnIndex(tb_LIST)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(tb_STATUS, status);
        db.update(TASKS_TABLE, cv, tb_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(tb_NAME, task);
        db.update(TASKS_TABLE, cv, tb_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TASKS_TABLE, tb_ID + "= ?", new String[] {String.valueOf(id)});
    }
}
