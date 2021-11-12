package com.smn947.listascompartidas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smn947.listascompartidas.Adapters.ListsAdapter;
import com.smn947.listascompartidas.Adapters.TasksAdapter;
import com.smn947.listascompartidas.Models.ModelLists;
import com.smn947.listascompartidas.Models.ModelTasks;
import com.smn947.listascompartidas.Utils.TasksDatabase;

import java.util.Collections;
import java.util.List;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TasksActivity extends AppCompatActivity implements DialogCloseListener{
    private TasksDatabase db;
    private RecyclerView tasksRecyclerView;
    private TasksAdapter tasksAdapter;
    private FloatingActionButton fab;
    private List<ModelTasks> tasksList;
    private String list;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            list = extras.getString("list");
            if(list != null) {
                TextView tv = findViewById(R.id.listName);
                tv.setText(" "+list);
            }
        }

        db = new TasksDatabase(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TasksAdapter(db, TasksActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper_Task(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);

        tasksList = db.getAllTasks(list);
        Collections.reverse(tasksList);

        tasksAdapter.setTasks(tasksList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Log.d("OnAdd", list);
                bundle.putString("list", list);
                bundle.putString("task", null);

                AddTask fragment = AddTask.newInstance();
                fragment.setArguments(bundle);

                fragment.show(getSupportFragmentManager(), AddTask.TAG);
            }
        });

        /*Load Ads*/
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasksList = db.getAllTasks(list);
        Collections.reverse(tasksList);
        tasksAdapter.setTasks(tasksList);
        tasksAdapter.notifyDataSetChanged();
    }
}