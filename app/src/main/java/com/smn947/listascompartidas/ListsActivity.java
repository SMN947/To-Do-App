package com.smn947.listascompartidas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smn947.listascompartidas.Adapters.ListsAdapter;
import com.smn947.listascompartidas.Models.ModelLists;
import com.smn947.listascompartidas.Utils.ListsDatabase;

import java.util.Collections;
import java.util.List;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ListsActivity extends AppCompatActivity implements DialogCloseListener{

    private ListsDatabase db;
    private RecyclerView tasksRecyclerView;
    private ListsAdapter listsAdapter;
    private FloatingActionButton fab;
    private List<ModelLists> listsList;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        getSupportActionBar().hide();

        db = new ListsDatabase(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.listsRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listsAdapter = new ListsAdapter(db, ListsActivity.this);
        tasksRecyclerView.setAdapter(listsAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper_List(listsAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);

        listsList = db.getAllLists();
        Collections.reverse(listsList);

        listsAdapter.setTasks(listsList);

        fab.setOnClickListener(v -> AddList.newInstance().show(getSupportFragmentManager(), AddList.TAG));


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
        listsList = db.getAllLists();
        Collections.reverse(listsList);
        listsAdapter.setTasks(listsList);
        listsAdapter.notifyDataSetChanged();
    }
}