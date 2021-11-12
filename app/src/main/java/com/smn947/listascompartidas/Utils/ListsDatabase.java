package com.smn947.listascompartidas.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import com.smn947.listascompartidas.Models.ModelLists;



public class ListsDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String NAME = "SMN947_ListasCompartidas";
    private static final String LISTS_TABLE = "Listas";
    private static final String tb_ID = "id";
    private static final String tb_NAME = "list";
    private static final String CREATE_LIST_TABLE = "CREATE TABLE " + LISTS_TABLE + "(" + tb_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tb_NAME + " TEXT)";

    private static final String TASKS_TABLE = "Tareas";
    private static final String tb_T_LIST = "lista";
    private static final String tb_T_NAME = "tarea";
    private static final String tb_T_STATUS = "status";
    private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TASKS_TABLE + "(" + tb_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tb_T_LIST + " INTEGER," + tb_T_NAME + " TEXT," + tb_T_STATUS + " TEXT)";


    private SQLiteDatabase db;

    public ListsDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIST_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + LISTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ModelLists list){
        ContentValues cv = new ContentValues();
        cv.put(tb_NAME, list.getList());
        db.insert(LISTS_TABLE, null, cv);
    }

    public List<ModelLists> getAllLists(){
        List<ModelLists> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(LISTS_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ModelLists task = new ModelLists();
                        task.setId(cur.getInt(cur.getColumnIndex(tb_ID)));
                        task.setList(cur.getString(cur.getColumnIndex(tb_NAME)));
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

    public void updateList(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(tb_NAME, task);
        db.update(LISTS_TABLE, cv, tb_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteList(int id){
        db.delete(LISTS_TABLE, tb_ID + "= ?", new String[] {String.valueOf(id)});
    }
}
