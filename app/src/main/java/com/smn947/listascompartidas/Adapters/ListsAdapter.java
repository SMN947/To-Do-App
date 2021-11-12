package com.smn947.listascompartidas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smn947.listascompartidas.AddList;
import com.smn947.listascompartidas.ListsActivity;
import com.smn947.listascompartidas.Models.ModelLists;
import com.smn947.listascompartidas.R;
import com.smn947.listascompartidas.TasksActivity;
import com.smn947.listascompartidas.Utils.ListsDatabase;

import java.util.List;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder>{
    private List<ModelLists> todoList;
    final private ListsDatabase db;
    final private ListsActivity activity;

    public ListsAdapter(ListsDatabase db, ListsActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ModelLists item = todoList.get(position);
        holder.list.setText(item.getList());
        holder.list.setOnClickListener(view -> {
            TextView tv = view.findViewById(R.id.listName);
            Log.d("SMN947", "Clicked :) | " + tv.getText().toString());
            Intent intent = new Intent(activity.getBaseContext(), TasksActivity.class);
            intent.putExtra("list", tv.getText().toString());
            activity.startActivity(intent);
        });
        /*holder.list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //db.updateList(item.getId(), 1);
                } else {
                    //db.updateList(item.getId(), 0);
                }
            }
        });*/
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ModelLists> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ModelLists item = todoList.get(position);
        db.deleteList(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ModelLists item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("list", item.getList());
        AddList fragment = new AddList();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddList.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView list;

        ViewHolder(View view) {
            super(view);
            list = view.findViewById(R.id.listName);
        }
    }
}
