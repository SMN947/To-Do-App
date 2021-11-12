package com.smn947.listascompartidas;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.smn947.listascompartidas.Models.ModelLists;
import com.smn947.listascompartidas.Models.ModelTasks;
import com.smn947.listascompartidas.Utils.ListsDatabase;
import com.smn947.listascompartidas.Utils.TasksDatabase;

public class AddTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;

    private TasksDatabase db;
    private String list;
    public static AddTask newInstance(){
        return new AddTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = requireView().findViewById(R.id.newListText);
        newTaskSaveButton = getView().findViewById(R.id.newListButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){

            String task = bundle.getString("task");
            newTaskText.setText(task);
            if(task != null) {
                isUpdate = true;
                assert task != null;
                if(task.length()>0) {
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }
            }

            list = bundle.getString("list");

        }else{
            newTaskSaveButton.setEnabled(false);
            newTaskSaveButton.setTextColor(Color.GRAY);
        }

        db = new TasksDatabase(getActivity());
        db.openDatabase();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                Log.d("SMN947", "SaveTask: " + text);
                Log.d("SMN947", String.valueOf(finalIsUpdate));
                Log.d("SMN947", list);
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                }else {
                    ModelTasks task = new ModelTasks();
                    task.setTask(text);
                    task.setList(list);
                    task.setList(list);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
